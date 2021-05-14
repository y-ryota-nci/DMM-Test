package jp.co.nci.iwf.servlet.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.component.i18n.MessageCdHolder;
import jp.co.nci.iwf.jersey.SessionHolder;

/**
 * アクセスログ記録フィルター。
 * HTTPリクエストレベルでのフィルターとしてアクセスログを記録。
 */
public class AccessLogFilter implements Filter {
	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(AccessLogFilter.class);
	/** 除外URL */
	private Set<String> ignoreURLs = new HashSet<>();
	/** アクセスログサービス */
	private AccessLogService accessLog;
	/** キャッシュマネージャー */
	private CacheManager cm;
	/** リクエストされたメッセージCDを保持するクラス */
	private MessageCdHolder messageCdHolder;

	/** 初期化 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String ignoreURL = filterConfig.getInitParameter("ignoreURL");
		if (StringUtils.isNotEmpty(ignoreURL)) {
			String[] ignores = ignoreURL.split(",\\s*");
			ignoreURLs.addAll(Arrays.asList(ignores));
		}
		// アクセスログサービス
		accessLog = CDI.current().select(AccessLogService.class).get();
		// キャッシュマネージャー
		cm = CDI.current().select(CacheManager.class).get();
		// リクエストされたメッセージCDを保持するクラス
		messageCdHolder = CDI.current().select(MessageCdHolder.class).get();
	}

	/** フィルター適用 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 処理前のアクセスログを退避
		final Long prev = accessLog.loadAccessLogId();

		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;
		final String path = concat(req.getServletPath(), req.getPathInfo());
		final long start = System.currentTimeMillis();
		final LoginInfo login = CDI.current().select(SessionHolder.class).get().getLoginInfo();
		String corporationCode = "--";
		if (login != null && StringUtils.isNotEmpty(login.getCorporationCode()))
			corporationCode = login.getCorporationCode();
		String userAddedInfo = "--";
		if (login != null && StringUtils.isNotEmpty(login.getUserAddedInfo()))
			userAddedInfo = login.getUserAddedInfo();

		String url = null;
		if (!ignoreURLs.contains(path)) {
			url = path;
			log.debug("START {} {} [{}/{}]", req.getMethod(), url, corporationCode, userAddedInfo);
		}

		boolean success = false;
		try {
			chain.doFilter(req, res);

			if (res.getStatus() < 400) {
				success = true;
			}
		}
		catch (RuntimeException | IOException | ServletException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// 現リクエストのメッセージCDをクリア
			messageCdHolder.clearThreadLocal();

			// このアクセスログIDは AccessLogEndpointInterceptorにて
			// Endpointがアクセスログの対象だったときだけ採番されている
			Long accessLogId = accessLog.loadAccessLogId();

			// アクセスログの処理結果を非同期で書き込み
			// このEndpointがアクセスログ対象外の場合は、アクセスログが記録されないため、アクセスログIDがない。
			// アクセスログ対象外のEndpointでも、例外があった場合だけは強制的にアクセスログを書き込むようにする。
			if (!success && accessLogId == null ) {
				accessLogId = accessLog.writeEntry(req);
			}
			if (accessLogId != null) {
				accessLog.updateResult(accessLogId, success);
				accessLog.appendDetail(accessLogId, "HTTP" + res.getStatus());
			}

			// 処理前のアクセスログIDを復元
			accessLog.saveAccessLogId(prev);

			long now = System.currentTimeMillis();
			if (url != null) {
				log.debug("END   {} {} [{}/{}] -> {}msec", req.getMethod(), url, corporationCode, userAddedInfo, (now - start));
			}

			// キャッシュ間隔の更新
			try {
				if (now - cm.getTimestamp() > CacheManager.TEN_SECONDS) {
					cm.init();
				}
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private String concat(String...astr) {
		StringBuilder sb = new StringBuilder();
		for (String str : astr)
			if (str != null)
				sb.append(str);
		return sb.toString();
	}

	/** リソース解放 */
	@Override
	public void destroy() {
		if (ignoreURLs != null) ignoreURLs.clear();
	}
}
