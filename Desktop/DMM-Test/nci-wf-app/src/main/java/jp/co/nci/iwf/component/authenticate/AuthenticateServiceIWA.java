package jp.co.nci.iwf.component.authenticate;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;

/**
 * 統合Windows認証（Integrated Windows authentication） でログイン処理を行う。
 * ※動作前提として、IIS＋IEが必要です。
 *
 * 厳密には当サービスでの認証は行わなず、IISとIE間で統合Windows認証を行った結果を
 * 流用してログイン処理を行うだけである。
 * なお、システムプロパティでログイン画面が有効になっていれば、Windows統合認証が通らなくても
 * ログイン画面経由でDB認証が出来る。
 */
@BizLogic
@Named(AuthenticateService.IWA)
public class AuthenticateServiceIWA extends AuthenticateServiceBase {
	@Inject private Logger log;

	/**
	 * ログイン可能かを判定
	 * @return 認証結果
	 */
	@Override
	@Transactional
	public LoginResult tryLogin() {
		// IIS側からの認証結果である認証トークンからログインIDを抜き出し、
		// そのユーザがユーザマスタ等に存在するかを検証
		final String windowsUserId = getWindowsUserId();
		if (isNotEmpty(windowsUserId)) {
			return super.tryLogin(null, windowsUserId);
		}

		// IISから認証トークンが渡されていない、つまりWindows統合認証が通らなかった
		return LoginResult.NotAuthenticatedIWA;
	}

	/**
	 * ログイン可能かを判定 【パスワード判定無し】
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 認証結果
	 */
	@Override
	public LoginResult tryLogin(String corporationCode, String userAddedInfo) {
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// Windows統合認証では trayLogin()を使うこと
	}

	/**
	 * ログイン可能かを判定
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @param plainPassword パスワード（平文）
	 * @return 認証結果
	 */
	@Override
	public LoginResult tryLogin(String corporationCode, String userAddedInfo, String plainPassword) {
		return super.tryLogin(corporationCode, userAddedInfo);	// Windows統合認証でもログイン画面で操作が行われたら、そのユーザでログインする
	}

	/**
	 * IISによるWindows統合認証結果から、WindowsでのログインIDを返す
	 * @return WindowsでのログインID
	 */
	private String getWindowsUserId() {
		final HttpServletRequest req = CDI.current().select(HttpServletRequest.class).get();
		final String remoteUser = req.getRemoteUser();
		log.info("remoteUser={}", remoteUser);

		if (isNotEmpty(remoteUser)) {
			// IIS側からの認証結果である認証トークンからログインIDを抜き出し、
			// そのユーザがユーザマスタ等に存在するかを検証
			final String[] tokens = remoteUser.split("\\\\");
			if (tokens.length > 1) {
				return tokens[1];
			}
		}
		return null;
	}

	/**
	 * ログイン認証結果をもとに、セッション情報を書き換え【SSO認証トークンをHTTPリクエストから吸上げて認証】
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @return
	 */
	@Override
	public LoginInfo updateSessionIfSuccess(LoginResult result, String newLocaleCode) {
		// IIS側からの認証結果である認証トークンからログインIDをつかってログイン者情報を生成
		final String windowsUserId = getWindowsUserId();
		final String corporationCode = null;
		return super.updateSessionIfSuccess(result, newLocaleCode, corporationCode, windowsUserId);
	}

	/** シングルサインオン（SSO)認証を使うか */
	@Override
	public boolean isEnableSSO() {
		return true;
	}
}
