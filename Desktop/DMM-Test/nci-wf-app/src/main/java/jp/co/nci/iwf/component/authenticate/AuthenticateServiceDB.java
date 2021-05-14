package jp.co.nci.iwf.component.authenticate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LockFlag;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;

/**
 * デフォルトのユーザ認証サービス。
 * ローカルDBへ問合せし、企業コード＋ログインID＋パスワードでの認証可否判定を行う
 */
@BizLogic
@Named(AuthenticateService.DB)
public class AuthenticateServiceDB extends AuthenticateServiceBase {
	@Inject private Logger log;

	/**
	 * ログイン可能かを判定【SSO認証トークンをHTTPリクエストから吸上げて認証】
	 * @return 認証結果
	 */
	public LoginResult tryLogin() {
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// これはSSO認証用だから呼び出し禁止
	}

	/**
	 * ログイン可能かを判定 【パスワード判定無し】
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 認証結果
	 */
	public LoginResult tryLogin(String corporationCode, String userAddedInfo) {
		// DMM専用処理：ログイン済みユーザがユーザIDが同じで他企業へログインするなら、パスワード認証しなくても可とする
		if (isAuthenticated() && eq(userAddedInfo, sessionHolder.getLoginInfo().getUserAddedInfo())) {
				return super.tryLogin(corporationCode, userAddedInfo);
		}
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// DB認証では trayLogin(String, String, String)を使うこと
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
		// ログインIDチェック
		LoginResult result = super.tryLogin(corporationCode, userAddedInfo);

		// パスワードチェック
		if (LoginResult.isSuccess(result)) {
			final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(corporationCode, userAddedInfo);
			final WfvUserBelong userBelong = belongs.get(0);
			result = validatePassword(userBelong, plainPassword);
		}
		if (log.isInfoEnabled()) {
			log.info("login result..." + result.toString());
		}
		return result;
	}

	/**
	 * パスワードのバリデーションを実施
	 * @param user ユーザマスタ
	 * @param plainPassword 平文のパスワード
	 * @return
	 */
	private LoginResult validatePassword(WfvUserBelong user, String plainPassword) {
		final WfmUserPassword userPassword = getWfmUserPassowrd(user.getCorporationCode(), user.getUserCode());

		LoginResult result = LoginResult.Success;
		if (isEmpty(plainPassword) || userPassword == null) {
			// パスワード未登録
			result = LoginResult.InvalidPassword;
		}
		else if (LockFlag.ON.equals(userPassword.getLockFlag())) {
			// アカウントロックされている
			result = LoginResult.LockedOut;
		}
		else if (!SecurityUtils.hash(plainPassword).equals(userPassword.getPassword())) {
			// パスワード誤り
			result = LoginResult.InvalidPassword;
		}
		else if (isOutOfDate(userPassword)) {
			//パスワード期限切れはログイン可能だが、ログイン後のパスワード変更必須
//			result = LoginResult.PasswordOutOfDate;
			result = LoginResult.ChangePassword;
		}
		else if (result == LoginResult.Success
				&& CommonFlag.ON.equals(userPassword.getChangeRequestFlag())) {
			// 次回ログイン時にパスワード変更が必要
			result = LoginResult.ChangePassword;
		}

		// パスワードマスタを更新
		// メンテナンス中でログインできないのは操作者の過失ではないので、
		// メンテナンス中はパスワードマスタを更新しない
		if (userPassword != null && !eq(result, LoginResult.LoginForAdminOnly)) {
			// 成功なら最終ログイン時刻を記録し、ログインチャレンジ数をリセット
			// 失敗ならログインチャレンジ数を増やす
			boolean isNewAccountLock = updatePassword(userPassword, result);

			// 今回アカウントロックされたのであれば、メール通知
			Boolean mailNotification = propService.getBool(CorporationProperty.ACCOUNT_LOCK_NOTIFICATION);
			if (isNewAccountLock && mailNotification != null && mailNotification.booleanValue()) {
				sendAccountLockMail(user, userPassword);
			}
		}
		return result;
	}

	/**
	 * ログイン者情報を生成(セッションへ格納はしない)
	 * @param newLocaleCode 言語
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 生成されたログイン者情報
	 */
	@Override
	public LoginInfo createLoginInfo(String newLocaleCode, String corporationCode, String userAddedInfo) {
		final LoginInfo login = super.createLoginInfo(newLocaleCode, corporationCode, userAddedInfo);

		// パスワードをローカルDBで管理するための追加処理
		final WfmUserPassword userPassword = getWfmUserPassowrd(login.getCorporationCode(), login.getUserCode());
		if (userPassword != null) {
			if (isOutOfDate(userPassword)) {
				//パスワード期限切れはログイン可能だが、ログイン後のパスワード変更必須
				login.setChangePassword(true);
			}
			else if (CommonFlag.ON.equals(userPassword.getChangeRequestFlag())) {
				// 次回ログイン時にパスワード変更が必要
				login.setChangePassword(true);
			}
		}
		return login;
	}
}
