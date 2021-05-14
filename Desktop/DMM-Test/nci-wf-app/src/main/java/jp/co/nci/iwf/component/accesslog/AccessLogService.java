package jp.co.nci.iwf.component.accesslog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.StringTruncator;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.component.system.ThreadPoolService;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Request;
import jp.co.nci.iwf.endpoint.unsecure.login.LoginRequest;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.provider.JacksonConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLog;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLogDetail;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アクセスログ書き込みサービス
 */
@BizLogic
public class AccessLogService extends BaseService {
	/** アクセスログIDを事前採番しておく数 */
	private static final long ACCESSLOG_ID_SIZE = 30L;
	/** アクセスログ明細IDを事前採番しておく数 */
	private static final long ACCESSLOG_DETAIL_ID_SIZE = ACCESSLOG_ID_SIZE * 2L;
	/** 同一スレッドで記録されたアクセスログID。アクセスログ明細を登録する際に使用されます */
	private static final ThreadLocal<Long> IDS = new ThreadLocal<>();
	/** 改行コード */
	private static final String LF = "\r\n";

	@Inject
	private Logger log;
	@Inject
	private ThreadPoolService threadPool;
	@Inject
	private NumberingService numbering;
	@Inject
	private ManifestService manifest;
	@Inject
	private DestinationDatabaseService destination;
	@Inject
	private HttpServletRequest req;

	/** パフォーマンスを向上させるために事前に採番したアクセスログIDのリスト。 */
	private Queue<Long> queueIds = new ConcurrentLinkedQueue<>();
	/** パフォーマンスを向上させるために事前に採番したアクセスログ明細IDのリスト。 */
	private Queue<Long> queueDetailIds = new ConcurrentLinkedQueue<>();
	/** 書き込むべきアクセスログ内容のキュー */
	private Queue<Object> queueContents = new ConcurrentLinkedQueue<>();

	@PostConstruct
	public void init() {
	}

	/**
	 * パラメータをJSON化
	 * @param params パラメータの配列
	 * @return
	 */
	private String toJson(Object[] params) {
		if (params == null || params.length == 0)
			return "";

		final StringBuilder sb = new StringBuilder(128);
		for (Object p : params) {
			if (p == null)
				continue;

			// ログインリクエストではパスワードが平文で表示されてしまうため、パスワード部分だけマスキング
			if (p instanceof LoginRequest) {
				final LoginRequest src = (LoginRequest)p;
				if (isNotEmpty(src.getPassword())) {
					final LoginRequest dest = (LoginRequest)src.clone();
					dest.setPassword(dest.getPassword().replaceAll(".", "*"));
					p = dest;
				}
			}
			// パスワード変更画面のパスワードをマスキング
			if (p instanceof Au0011Request) {
				final Au0011Request src = (Au0011Request)p;
				if (isNotEmpty(src.newPassword1) && isNotEmpty(src.newPassword2)) {
					final Au0011Request dest = (Au0011Request)src.clone();
					dest.newPassword1 = dest.newPassword1.replaceAll(".", "*");
					dest.newPassword2 = dest.newPassword2.replaceAll(".", "*");
					p = dest;
				}
			}

			if (sb.length() > 0) sb.append(LF);
			final Class<?> c = p.getClass();
			if (p instanceof CharSequence) {
				sb.append(p);
			}
			else if (p instanceof BodyPart) {
				sb.append( "(multipart/form-data)" );
			}
			else if (p instanceof List) {
				List<?> list = (List<?>)p;
				if (!list.isEmpty()) {
					Object obj = list.get(0);
					if (obj instanceof BodyPart) {
						sb.append( "(multipart/form-data)" );
					}
					else {
						if (sb.length() > 0) sb.append(LF);
						try {
							sb.append( JacksonConfig.getObjectMapper().writeValueAsString(p) );
						}
						catch (JsonProcessingException e) {
							throw new BadRequestException(e);
						}
					}
				}
			}
			else if (p instanceof CharSequence || c.isPrimitive() || p instanceof Number) {
				sb.append(p);
			}
			else if ((p instanceof ServletRequest) || (p instanceof ServletResponse) || (p instanceof ServletContext)){
				continue;
			}
			else {
				try {
					sb.append( JacksonConfig.getObjectMapper().writeValueAsString(p) );
				}
				catch (JsonProcessingException e) {
					throw new BadRequestException(e);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * アクセスログを書き込み
	 * @param req HTTPサーブレットリクエスト
	 * @return アクセスログID。アクセスログ明細を登録する際に親キーとして使用されます。
	 */
	public long writeEntry(HttpServletRequest req) {
		final ScreenInfo inf = MiscUtils.toScreenInfo(req);
		final String screenId = inf.getScreenId();
		final String actionName = inf.getActionName();
		final String screenName = toTitle(screenId);

		final MwtAccessLog entity = new MwtAccessLog();
		entity.setAccessTime(timestamp());
		entity.setUri(truncate(req.getRequestURI(), 2000));
		entity.setSessionId(truncate(req.getSession().getId(), 32));
		entity.setUserAgent(truncate(req.getHeader("user-agent"), 512));
		entity.setOpeIpAddress(truncate(req.getRemoteAddr(), 40));
		entity.setScreenId(truncate(screenId, 90));
		entity.setScreenName(truncate(screenName, 300));
		entity.setActionName(truncate(actionName, 90));
		entity.setAccessLogResultType(AccessLogResultType.UNKNOWN);	// INSERT時は常に不明として書き込む
		entity.setDeleteFlag(DeleteFlag.OFF);
		entity.setAppVersion(truncate(manifest.getVersion(), 90));
		entity.setDbConnectString(truncate(destination.getUrl() + "@" + destination.getUser(), 300));
		entity.setHostIpAddress(truncate(req.getLocalAddr(), 40));
		entity.setHostPort(req.getLocalPort());
		entity.setThreadName(Thread.currentThread().getName());

		// ログイン済みならセッション情報も追記
		if (sessionHolder != null) {
			final LoginInfo login = sessionHolder.getLoginInfo();
			if (login != null) {
				entity.setOpeCorporationCode(truncate(login.getCorporationCode(), 10));
				entity.setOpeUserAddedInfo(truncate(login.getUserAddedInfo(), 50));
				entity.setOpeUserCode(truncate(login.getUserCode(), 25));
				entity.setSpoofingCorporationCode(truncate(login.getSpoofingCorporationCode(), 10));
				entity.setSpoofingUserCode(truncate(login.getSpoofingUserCode(), 25));
				entity.setSpoofingUserAddedInfo(truncate(login.getSpoofingUserAddedInfo(), 50));
			}
		}

		// アクセスログIDを採番
		Long accessLogId = loadAccessLogId();
		if (accessLogId == null) {
			accessLogId = nextAccessLogId();
			saveAccessLogId(accessLogId);
		}
		entity.setAccessLogId(accessLogId);

		// アクセスログへの記録内容をキューに入れ、別スレッドで非同期実行
		queueContents.offer(entity);
		threadPool.submit(new AccessLogTask(queueContents));

		// 同一スレッド内で参照可能なようにアクセスログIDを返却
		return accessLogId;
	}

	/**
	 * アクセスログとして例外情報を記録
	 * @param e 例外
	 * @param params
	 */
	public void appendException(Throwable e) {
		// アクセスログIDが記録されていなければ、新たにエントリを生成
		Long accessLogId = loadAccessLogId();
		if (accessLogId == null) {
			accessLogId = writeEntry(req);
		}
		appendException(accessLogId, e);
	}
	/**
	 * アクセスログとして例外情報を記録
	 * @param accessLogId アクセスログID
	 * @param e 例外
	 * @param params
	 */
	public void appendException(long accessLogId, Throwable e) {
		if (accessLogId != 0L && e != null) {
			// スタックトレースを文字列化
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			String contents = sw.toString();

			// アクセスログ明細を非同期でインサート
			final MwtAccessLogDetail detail = new MwtAccessLogDetail();
			detail.setAccessLogDetailId(nextAccessLogDetailId());
			detail.setAccessLogId(accessLogId);
			detail.setAccessTime(timestamp());
			detail.setKeyValue(contents);
			detail.setDeleteFlag(DeleteFlag.OFF);

			// アクセスログへの記録内容をキューに入れ、別スレッドで非同期実行
			queueContents.offer(detail);
			threadPool.submit(new AccessLogTask(queueContents));
		}
	}

	/**
	 * アクセスログとして追加情報を記録
	 * @param params パラメータ配列
	 * @param params
	 */
	public void appendDetail(Object...params) {
		Long accessLogId = loadAccessLogId();
		appendDetail(accessLogId, params);
	}

	/**
	 * アクセスログとして追加情報を記録
	 * @param accessLogId アクセスログID
	 * @param params パラメータ配列
	 * @param params
	 */
	public void appendDetail(Long accessLogId, Object...params) {
		String contents = toJson(params);
		if (accessLogId != null && isNotEmpty(contents)) {
			// アクセスログ明細を非同期でインサート
			final MwtAccessLogDetail detail = new MwtAccessLogDetail();
			detail.setAccessLogDetailId(nextAccessLogDetailId());
			detail.setAccessLogId(accessLogId);
			detail.setAccessTime(timestamp());
			detail.setKeyValue(contents);
			detail.setDeleteFlag(DeleteFlag.OFF);

			// アクセスログへの記録内容をキューに入れ、別スレッドで非同期実行
			queueContents.offer(detail);
			threadPool.submit(new AccessLogTask(queueContents));
		}
	}

	/**
	 * 画面IDから画面タイトルを返す
	 * @param screenId
	 * @return
	 */
	private String toTitle(String screenId) {
		final MessageCd cd;
		try {
			cd = MessageCd.valueOf(screenId);
		}
		catch (IllegalArgumentException e) {
			return "NotDefined";
		}
		String title = i18n.getText(cd);
		return defaults(title, "");
	}

	/**
	 * このスレッドのアクセスログIDを返す
	 * @return
	 */
	public Long loadAccessLogId() {
		return IDS.get();
	}

	/**
	 * このスレッドのアクセスログIDを設定
	 * （パッケージ内専用）
	 * @return
	 */
	public void saveAccessLogId(Long id) {
		IDS.set(id);
	}

	/** アクセスログIDを採番して返す */
	private long nextAccessLogId() {
		Long accessLogId = queueIds.poll();
		if (accessLogId == null) {
			synchronized (queueIds) {
				accessLogId = queueIds.poll();
				if (accessLogId == null) {
					// 採番済みのアクセスログIDがなければ一括採番し、その結果をすべてキューに突っ込む
					final long start = numbering.newPK(MwtAccessLog.class, ACCESSLOG_ID_SIZE);
					for (long i = 0; i < ACCESSLOG_ID_SIZE; i++) {
						queueIds.add(start + i);
					}
					// キューからアクセスログIDを取得
					accessLogId = queueIds.poll();
				}
			}
		}
		return accessLogId.longValue();
	}

	/** アクセスログ明細IDを採番 */
	private long nextAccessLogDetailId() {
		Long accessLogDetailId = queueDetailIds.poll();
		if (accessLogDetailId == null) {
			synchronized(queueDetailIds) {
				accessLogDetailId = queueDetailIds.poll();
				if (accessLogDetailId == null) {
					// 採番済みのアクセスログIDがなければ一括採番し、その結果をすべてキューに突っ込む
					final long start = numbering.newPK(MwtAccessLogDetail.class, ACCESSLOG_DETAIL_ID_SIZE);
					for (long i = 0; i < ACCESSLOG_DETAIL_ID_SIZE; i++) {
						queueDetailIds.add(start + i);
					}
					// キューからアクセスログ明細IDを取得
					accessLogDetailId = queueDetailIds.poll();
				}
			}
		}
		return accessLogDetailId.longValue();
	}

	/**
	 * アクセスログを更新
	 * @param accessLogId アクセスログID
	 * @param success 処理結果が成功ならtrue
	 */
	public void updateResult(Long accessLogId, boolean success) {
		// アクセスログを非同期で書き込み
		if (accessLogId != null) {
			MwtAccessLog header = new MwtAccessLog();
			header.setAccessLogId(accessLogId);
			header.setAccessLogResultType(success ? AccessLogResultType.SUCCESS : AccessLogResultType.FAIL);

			// アクセスログへの記録内容をキューに入れ、別スレッドで非同期実行
			queueContents.offer(header);
			log.trace("queue.size()={} success={}", queueContents.size(), success);
			threadPool.submit(new AccessLogTask(queueContents));
		}
	}

	/** 長すぎる文字をカット */
	private static String truncate(String s, int len) {
		return StringTruncator.trunc(s, len, StringTruncator.UTF8);
	}

}
