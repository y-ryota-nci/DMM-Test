package jp.co.nci.iwf.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.nci.iwf.component.CodeBook.AppURL;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.LoginResult;

/**
 * URLにコンテキストルートを指定したときに、適切なURLへフォワードするServlet。
 * ようは welcome-fileの代わり、directoryIndex的なもの。
 */
public class DirectoryIndexServlet extends HttpServlet {
	private AuthenticateService auth;

	/** 初期化 */
	public void init(ServletConfig config) throws ServletException {
		auth = AuthenticateService.get();
	}

	/**
	 * GETアクセス
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		if (res.getStatus() < 400) {
			if (!auth.isAuthenticated() && auth.isEnableSSO()) {
				// 未認証でSSO認証ありなら、ログイン画面なしでダイレクトに認証を行ってセッションのログイン情報を書き換える
				authenticateSSO(auth, req);
			}

			String uri = null;
			if (auth.isAuthenticated()) {
				// ログイン済みならその人のトップページ
				uri = LoginInfo.get().getTopPageUrl();
			}
			else if (auth.isUseLoginScreen()) {
				// 未ログインかつログイン画面を使うなら、ログイン画面
				uri = AppURL.LOGIN;
			}

			// 適切なトップページへ遷移させる
			// どこにも遷移先がなければ HTTP401 Unauthorized
			if (uri != null)
				res.sendRedirect("page/" + uri);
			else
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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

}
