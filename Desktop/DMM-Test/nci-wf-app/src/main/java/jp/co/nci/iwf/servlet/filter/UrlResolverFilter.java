package jp.co.nci.iwf.servlet.filter;

import java.io.IOException;

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

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.LoginResult;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 遷移先URL解決フィルタ（*.html専用）。
 * ・未ログインでログイン不要なURLなら要求URLへ。
 * ・未ログインでログインが必要なURLならログイン画面へ。
 * ・ログイン済みで初回リクエストURLありならそのURLへ。
 * ・ログイン済みで初回リクエストURLなしなら要求URLへ。
 */
public class UrlResolverFilter implements Filter, CodeBook {
	private String baseURL;
	private String changePasswordURL;
	private String loginURL;
	private String spoofingURL;
	private Logger log = LoggerFactory.getLogger(UrlResolverFilter.class);
	private CorporationPropertyService prop;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String contextPath = filterConfig.getServletContext().getContextPath();
		if (StringUtils.isEmpty(contextPath) || "/".equals(contextPath))
			baseURL = "/page/";
		else
			baseURL = contextPath + "/page/";

		loginURL = baseURL + AppURL.LOGIN;
		changePasswordURL = baseURL + AppURL.CHANGE_PASSWORD;
		spoofingURL = baseURL + AppURL.SPOOFING_PAGE;
		prop = CDI.current().select(CorporationPropertyService.class).get();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;
		final String uri = req.getRequestURI();

		LoginInfo login = LoginInfo.get();
		final AuthenticateService auth = AuthenticateService.get();
		if (!auth.isAuthenticated() && auth.isEnableSSO()) {
			// 未認証でSSO認証ありなら、ログイン画面なしでダイレクトに認証を行ってセッションのログイン情報を書き換える
			login = authenticateSSO(auth, req);
		}

		if (isLoginURL(uri)) {
			// ログイン画面が要求されたが、システム的にログイン画面は無効になっているのでエラー扱い
			if (!auth.isUseLoginScreen()) {
				disableLoginScreen(res);
				return;
			}
		}
		else if (isLoginFreeDir(uri) || isSystemError(uri)) {
			;	// ログイン不要画面/システムエラーはそのまま遷移
		}
		else if (auth.isAuthenticated()) {
			// 認証済み
			if (login.isChangePassword()) {
				// パスワード変更要求があれば、パスワード変更画面へ強制遷移
				redirectToChangePassword(res, uri);
			}
			else if (isRequireSpoofing(login))
				// なりすまし要求があれば、なりすまし先選択画面へ遷移
				redirectToSpoofing(res, uri);
			else {
				// ログイン済みで初回リクエストがあれば、そちらへリダイレクト。なければ要求通りのURLへ。
				redirectToRestoreURL(res);
			}
		}
//		else if (auth.isUseLoginScreen()) {
//			// 未認証なら、初回要求URLを保存しつつログイン画面へ遷移
//			redirectToLogin(req, res);
//		}
		else if (auth.isUseLoginScreen() && "mail".equals(req.getParameter(QueryString.FROM))) {
			// メールのリンク押下で遷移してきたら、HTTP401エラーとせず、そのままログイン画面へ遷移
			redirectToLogin(req, res);
		}
		else {
			// 認証方法がないので、HTTP401エラーを返す
			noAuthenticationMethod(req, res);
			return;
		}

		// .htmlファイルによるアクセス権限判定
		// これを有効化するとリクエスト自体がアクセスログに何も記録されなくなってしまうので
		// Endpoint側でのみ記録アクセス権限判定をする。
//		ScreenAuthorityService sa = CDI.current().select(ScreenAuthorityService.class).get();
//		if (login != null && auth.isAuthenticated() && !sa.isAuthorized(login, req)) {
//			notAuthorizedError(req, res, login);
//			return;
//		}

		chain.doFilter(request, response);
	}

	/** アクセス権限なし */
	@SuppressWarnings("unused")
	private void notAuthorizedError(HttpServletRequest req, HttpServletResponse res, LoginInfo login) throws IOException {
		res.sendError(HttpServletResponse.SC_FORBIDDEN);
		String msg;
		if (login == null) {
			// 未ログインでここに来るはずはないのだが、一応
			msg = "URLへのアクセス権がありません。URL=" + req.getRequestURI();
		}
		else {
			msg = "URLへのアクセス権がありません。URL=" + req.getRequestURI()
					+ " corporationCode=" + login.getCorporationCode()
					+ " userAddedInfo=" + login.getUserAddedInfo();
		}
		// アクセスログも書く
		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		final Long accessLogId = accesslog.loadAccessLogId();
		if (accessLogId != null) {
			accesslog.updateResult(accessLogId, false);
			accesslog.appendDetail(accessLogId, msg);
		}
	}

	/** なりすまし先選択画面への遷移が必要か */
	private boolean isRequireSpoofing(LoginInfo login) {
		// なりすまし要求ありであること
		// なりすまし機能が有効であること
		return login.isRequireSpoofing()
				&& prop.getBool(CorporationProperty.ALLOW_ADMIN_TO_IMPERSONATE, false);
	}

	/** 認証方法がない */
	private void noAuthenticationMethod(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
		// 既存エラーがなければ、認証エラーを返す
		final String msg = "認証できませんでした";
		if (res.getStatus() < 400) {
			log.error(msg);

			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		// 初回要求URLを保存
		if (canSaveRequestUri(req)) {
			final AuthenticateService auth = AuthenticateService.get();
			auth.saveFirstRequestURL(req);
		}

		// アクセスログも書く
		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		final Long accessLogId = accesslog.loadAccessLogId();
		if (accessLogId != null) {
			accesslog.updateResult(accessLogId, false);
			accesslog.appendDetail(accessLogId, msg);
		}
	}

	/** ログイン画面が要求されたが、ログイン画面が無効 */
	private void disableLoginScreen(final HttpServletResponse res) throws IOException {
		final String msg = "ログイン画面が要求されましたが、システムプロパティでログイン画面は無効化されています。";

		// 既存エラーがなければHTTP410 GONEを返す
		if (res.getStatus() < 400) {
			log.error(msg);
			res.sendError(HttpServletResponse.SC_GONE);
		}

		// アクセスログも書く
		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		final Long accessLogId = accesslog.loadAccessLogId();
		if (accessLogId != null) {
			accesslog.updateResult(accessLogId, false);
			accesslog.appendDetail(accessLogId, msg);
		}
	}

	/** SSO認証を行い、ログイン者情報を生成 */
	private LoginInfo authenticateSSO(AuthenticateService auth, HttpServletRequest req) {
		// ログイン認証判定
		final LoginResult result = auth.tryLogin();	// SSO認証ならRequestから勝手に必要な値を吸い上げて認証するのでパラメータ不要

		// ログイン認証が成功ならセッションを書き換え、失敗ならアクセスログを記録
		final String newLocaleCode = null;
		final LoginInfo loginInfo = auth.updateSessionIfSuccess(result, newLocaleCode);

		return loginInfo;
	}

	/** なりすまし先選択画面へ遷移 */
	private void redirectToSpoofing(HttpServletResponse res, final String uri) throws IOException {
		if (!uri.endsWith(spoofingURL)) {
			log.info("パスワード変更要求あり、パスワード変更画面へ強制遷移します=" + AppURL.CHANGE_PASSWORD);
			res.sendRedirect(spoofingURL);
		}
	}

	/** 初回要求URLを保存しつつ、ログイン画面へ遷移 */
	private void redirectToLogin(HttpServletRequest req, final HttpServletResponse res) throws IOException {
		// 初回要求URLを保存
		if (canSaveRequestUri(req)) {
			final AuthenticateService auth = AuthenticateService.get();
			auth.saveFirstRequestURL(req);
		}
		log.info("未ログインのため、ログイン画面へ遷移します=" + loginURL);
		res.sendRedirect(loginURL);
	}

	/** 初回要求URLを復元して遷移 */
	private void redirectToRestoreURL(final HttpServletResponse res) throws IOException {
		final AuthenticateService auth = AuthenticateService.get();
		final String redirect = auth.getFirstRequestURL();
		if (StringUtils.isNotEmpty(redirect)) {
			log.info("ログイン済み直後リクエストのため、初回リクエスト画面へ遷移します=" + redirect);
			res.sendRedirect(baseURL + redirect);
		}
	}

	/** パスワード変更画面へ遷移 */
	private void redirectToChangePassword(final HttpServletResponse res, final String uri) throws IOException {
		if (!uri.endsWith(changePasswordURL)) {
			log.info("パスワード変更要求あり、パスワード変更画面へ強制遷移します=" + AppURL.CHANGE_PASSWORD);
			res.sendRedirect(changePasswordURL);
		}
	}

	/** ログイン画面が要求されているか？ */
	private boolean isLoginURL(String path) {
		return path.indexOf(loginURL) >= 0;
	}

	/** ログイン不要なディレクトリへのアクセスか？ */
	private boolean isLoginFreeDir(String path) {
		return path.indexOf(AppURL.LOGIN_FREE_DIR) >= 0;
	}

	/** システムエラー画面が要求されているか？ */
	private boolean isSystemError(String path) {
		return path.indexOf(AppURL.SYSTEM_ERROR) >= 0;
	}

	/** 初回要求URIとして保存すべきURIか */
	private boolean canSaveRequestUri(final HttpServletRequest req) {
		// ログイン不要な画面／エラー画面／ポップアップ画面でないこと
		final String path = req.getRequestURI();
		final String queryString = req.getQueryString();
		return path.indexOf(AppURL.LOGIN_FREE_DIR) < 0
				&& path.indexOf(AppURL.ERROR_DIR) < 0
				&& (MiscUtils.isEmpty(queryString) || queryString.indexOf("&_popup=1") < 0);
	}

	@Override
	public void destroy() {
		baseURL = null;
		changePasswordURL = null;
		loginURL = null;
		spoofingURL = null;
		log = null;
		prop = null;
	}

}
