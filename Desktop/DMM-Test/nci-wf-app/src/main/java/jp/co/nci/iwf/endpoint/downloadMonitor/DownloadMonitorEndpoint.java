package jp.co.nci.iwf.endpoint.downloadMonitor;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ダウンロード状況を監視するためのEndpoint
 */
@Path("/download-monitor")
@Endpoint
public class DownloadMonitorEndpoint {
	@Inject
	private Logger log;
	@Context
	private HttpServletRequest req;
	@Inject
	private SessionHolder sessionHolder;


	/** 開始待ち／完了待ちのタイムアウト(ミリ秒) */
	private long timeout;

	/** 監視間隔（ミリ秒） */
	private static final long WAIT = 300L;

	/** 監視のデフォルトタイムアウト(ミリ秒) */
	private static final long DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(10);

	/** 監視開始までのデフォルトタイムアウト(ミリ秒) */
	private static final long DEFAULT_STARTUP_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

	/** DownloadMonitorEndpointの処理結果定数 */
	protected interface Result {
		/** 成功 */
		String SUCCESS = "success";
		/** タイムアウト */
		String TIMEOUT = "timeout";
		/** スレッド割り込みによる中断 */
		String INTERRUPTED = "interrupted";
		/** パラメータ不正 */
		String INVALID_PARAMS = "invalidParameters";
	}

	/**
	 * ダウンロードが完了したかどうかを確認する処理
	 * @param token クライアントから受信した監視対象を識別するためのパラメータ 'token'
	 * @param timeoutMinutes タイムアウト（省略可）。0ならタイムアウトしない
	 * @return メッセージ
	 */
	@Path("/watch")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response watch(
			@QueryParam("token") String token,
			@QueryParam("timeout") Integer timeout){

		if (MiscUtils.isEmpty(token))
			throw new BadRequestException("tokenが送信されていません。");

		log.debug("watch() start [{}] timeout={}", token, timeout);
		String result = Result.INVALID_PARAMS;
		try {
			// タイムアウトの設定
			this.timeout = timeout == null ? DEFAULT_TIMEOUT : timeout.longValue();

			// トークンが送られてきているか？
			result = Result.INVALID_PARAMS;
			if (StringUtils.isEmpty(token)) {
				return toResponse(Status.BAD_REQUEST, result);
			}

			// Service側で処理開始するまで待機
			result = waitUntilStart(token);
			if (!Result.SUCCESS.equals(result)) {
				return toResponse(Status.ACCEPTED, result);
			}

			// Service側で処理が終わるかタイムアウトするまで待機
			result = waitUntilComplete(token);
			if (!Result.SUCCESS.equals(result)) {
				return toResponse(Status.ACCEPTED, result);
			}

			return toResponse(Status.OK, result);
		}
		finally {
			log.debug("watch() end [{}] -> {}", token, result);
		}
	}

	/**
	 * 処理が完了するまで、またはタイムアウトするまで待機
	 * @param token トークン
	 * @return
	 */
	private String waitUntilComplete(final String token) {
		long start = System.currentTimeMillis();
		while (isProcessing(token)) {
			// 高負荷時にはDownloadMonitorの開始より前に監視対象の処理が終わっている可能性がある。
			// 処理履歴をチェックして、完了していれば成功としよう。
			if (isAreadyPrinted(token)) {
				log.debug("... モニター対象の処理はすでに完了しています。　(token='{}')", token);
				return Result.SUCCESS;
			}
			// タイムアウトしていたら終わり(0はタイムアウトしないことにするが使用には注意を! sahashi)
			long elapsedTime = System.currentTimeMillis() - start;
			if ((0 < timeout) && (timeout < elapsedTime)) {
				log.warn("...モニター対象の処理完了を待っていしましたが、タイムアウトしました。　(token='{}')", token);
				return Result.TIMEOUT;
			}
			log.trace("...モニター対象の処理完了を待っています。　(token='{}'), elapsedTime={}", token, elapsedTime);

			if (!sleep(WAIT)) {
				return Result.INTERRUPTED;
			}
		}
		return Result.SUCCESS;
	}

	/**
	 * 処理が開始されるまで、またはタイムアウトするまで待機する
	 * @param token トークン
	 * @return
	 */
	private String waitUntilStart(final String token) {
		long start = System.currentTimeMillis();
		while (!isProcessing(token)) {
			// 高負荷時にはDownloadMonitorの開始より前に監視対象の処理が終わっている可能性がある。
			// 処理履歴をチェックして、完了していれば成功としよう。
			if (isAreadyPrinted(token)) {
				log.debug("...モニター対象の処理はすでに終わっています。　(token='{}')", token);
				return Result.SUCCESS;
			}
			// タイムアウトしていたら終わり(0はタイムアウトしないことにするが使用には注意を! sahashi)
			if ((0 < timeout) && (System.currentTimeMillis() - start > DEFAULT_STARTUP_TIMEOUT)) {
				log.warn("...モニター対象の処理開始を待っていましたが、タイムアウトしました。　(token='{}')。もしかして、モニター対象側で DownloadNotifyService#begin()し忘れていませんか？", token);
				return Result.TIMEOUT;
			}
			log.trace("...モニター対象の処理開始をまっています。　(token='{}')", token);
			if (!sleep(WAIT)) {
				return Result.INTERRUPTED;
			}
		}
		return Result.SUCCESS;
	}

	/**
	 * Thread.sleep()のラッパ
	 * @param wait
	 */
	private boolean sleep(long wait) {
		try {
			Thread.sleep(wait);
			return true;
		}
		catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			// InterruptedExceptionをキャッチしてしまうとこのスレッドの
			// interrupt 状態をクリアしてしまうため、interrupt()しなおし
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * 指定されたトークンが処理中かを返す
	 * @param token トークン
	 * @return
	 */
	private boolean isProcessing(final String token) {
		// Serviceスレッド側で処理しているトークンが監視対象のトークンか？
		return sessionHolder.getDownloadingQueues().contains(token);
	}

	/**
	 * 処理履歴を調べて、指定されたトークンが処理完了していないかを返す
	 *
	 * @param token トークン
	 * @return 印刷完了していれば true
	 */
	private boolean isAreadyPrinted(final String token) {
		return sessionHolder.getDownloadedHistory().contains(token);
	}

	private Response toResponse(Status status, String result) {
		return Response.status(status).entity(result).build();
	}
}

