package jp.co.nci.iwf.component.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.enterprise.inject.spi.CDI;
import javax.mail.AuthenticationFailedException;
import javax.mail.SendFailedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.system.ThreadPoolService;

/**
 * メール送信を非同期実行するためのヘルパークラス.
 */
class SendMailHelper implements Callable<Integer> {
	private Logger log = LoggerFactory.getLogger(SendMailHelper.class);

	/** 送信メールリスト. */
	private List<Mail> sendMailList = null;
	/** 送信時の「メールテンプレートの置換Map」 */
	private Map<String, String> variables;
	/** メール送信後に呼び出されるコールバック関数 */
	private IMailCallback callback = null;
	/** メール送信の起点となったオペレーションのアクセスログID */
	private Long accessLogId;
	/** アクセスログサービス */
	private AccessLogService accessLog;

	/**
	 * コンストラクタ
	 * 外部からの呼出不可
	 * @param sendMailList 送信内容リスト
	 * @param variables 送信時の「メールテンプレートの置換Map」
	 * @param callback メール送信後に呼び出されるコールバック関数
	 * @param accessLogId メール送信の起点となったオペレーションのアクセスログID
	 */
	private SendMailHelper(List<Mail> sendMailList, Map<String, String> variables, IMailCallback callback, Long accessLogId) {
		this.sendMailList = sendMailList;
		this.variables = variables;
		this.callback = callback;
		this.accessLogId = accessLogId;
		this.accessLog = CDI.current().select(AccessLogService.class).get();
	}

	/** 非同期実行用のスレッドプール */
	private static ThreadPoolService getPool() {
		return CDI.current().select(ThreadPoolService.class).get();
	}

	/**
	 * 非同期メール送信
	 * @param sendMailList 送信内容リスト
	 * @param variables 送信時の「メールテンプレートの置換Map」
	 * @param callback メール送信後に呼び出されるコールバック関数
	 * @param accessLogId メール送信の起点となったオペレーションのアクセスログID
	 */
	public static Future<Integer> sendAsync(List<Mail> sendMailList, Map<String, String> variables, IMailCallback callback, Long accessLogId) throws ExecutionException, InterruptedException {
		final SendMailHelper task = new SendMailHelper(sendMailList, variables, callback, accessLogId);
		return getPool().submit(task);
	}

	/**
	 * 非同期送信用Callableインターフェース
	 */
	@Override
	public Integer call() throws Exception {
		int sent = 0;
		if (sendMailList != null) {
			for (Mail mail : sendMailList) {
				try {
					// 前処理
					beforeSend(mail);

					SendMail sendMail = new SendMail();
					sendMail.send(mail);
					++sent;

					// 後処理
					afterSent(mail, true, null);
				}
				catch (Exception e) {
					// 後処理
					afterSent(mail, false, e);

					if (e instanceof SendFailedException) {
						// メールアドレスへの送信が出来なかった：他のメールは送信できるかもしれない
						final String msg = "メールアドレスへの送信が出来ませんでした（処理は続行します）";
						log.warn(msg, e);
						continue;
					}
					if (e instanceof IOException || e instanceof FileNotFoundException) {
						// 添付ファイルの読み込みやエンコードに失敗：次のメールは送信できるかもしれない
						final String msg = "添付ファイルの扱いに失敗しました（処理は続行します）";
						log.warn(msg, e);
						continue;
					}
					if (e instanceof AuthenticationFailedException) {
						// ユーザID／パスワード違いなどにより認証が失敗：後続処理は中止
						final String msg = "ユーザID／パスワード違いなどにより認証が失敗：後続処理は中止";
						log.error(msg, e);
						throw e;
					}

					// その他はシステムエラー扱いなので後続中止。
					final String msg = "一般的なメール送信エラー：後続処理は中止";
					log.error(msg, e);
					throw e;
				}
			}
		}
		return sent;
	}

	/** メール送信前処理 */
	private void beforeSend(Mail mail) {
		// メール送信内容をアクセスログ明細に記録
		writeAccessLogDetail(mail.toMap());
	}

	/**
	 * コールバック関数の呼び出し
	 * @param mail 送信内容
	 * @param success 成功時ならtrue
	 */
	private void afterSent(Mail mail, boolean success, Exception ex) {

		// コールバック関数
		if (callback != null) {
			try {
				callback.onSent(variables, mail, success);
			}
			catch (Exception e) {
				// コールバック中の例外もあり得るが、非同期実行中は例外をスローしてもしょうがないので
				// ログ出力して終わり。そこまでは面倒見れないよ
				log.error(e.getMessage(), e);
				// 例外をアクセスログ明細に記録
				writeAccessLogDetail(e);
			}
			finally {
				// リソース解放処理
				try { callback.dispose(); }
				catch (Exception e) {}
			}
		}
		// 例外をアクセスログ明細に記録
		if (ex != null) {
			writeAccessLogDetail(ex);
		}

		// リソース解放
		dispose();
	}

	/** リソース解放 */
	private void dispose() {
		log = null;
		if (sendMailList != null) {
			sendMailList.clear();
			sendMailList = null;
		}
		if (variables != null) {
			variables.clear();
			variables = null;
		}
		callback = null;
		accessLogId = null;
		accessLog = null;
	}

	/** アクセスログ明細を書き込み */
	private void writeAccessLogDetail(Object obj) {
		try {
			accessLog.appendDetail(accessLogId, obj);
		}
		catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}

	/** 例外をアクセスログ明細に書き込み */
	private void writeAccessLogDetail(Exception e) {
		try {
			accessLog.appendException(e);
		}
		catch (Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
	}
}
