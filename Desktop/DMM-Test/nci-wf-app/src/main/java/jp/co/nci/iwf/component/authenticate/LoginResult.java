package jp.co.nci.iwf.component.authenticate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ログイン認証の結果定数
 */
public enum LoginResult {
	/** 成功 */
	Success,
	/** すでにログイン済み */
	AlreadyLoggedIn,
	/** ユーザマスタに存在せず */
	NotExistUser,
	/** マスタ上でユニークなはずのログインIDが重複している */
	DupulicatedUserAddedInfo,
	/** 不正なログインID */
	InvalidUserAddedInfo,
	/** 所属組織／役職なし */
	InvalidUserBelong,
	/** 所属組織／役職の期限切れ */
	UserBelongOutOfDate,
	/** アクセス可能なメニューなし */
	NoAccessibleMenu,
	/** メニューロールCDを所有してない */
	NoMenuRoleCd,
	/** パスワード未登録 */
	InvalidPassword,
	/** アカウントロックされている */
	LockedOut,
	/** パスワード期限切れ */
	PasswordOutOfDate,
	/** パスワードは正しいが期限切れのため、パスワード変更が必要 */
	ChangePassword,
	/** (メンテナンス中で)管理者以外ログイン不可 */
	LoginForAdminOnly,
	/** 認証サービスはあるが、中身が未実装 */
	NotImplemented,
	/** LDAPサーバ側で認証が通らなかった */
	NotAuthenticatedLDAP,
	/** Windows統合認証(IIS)側で認証が通らなかった */
	NotAuthenticatedIWA,
	/** 不明な理由 */
	UnknownReason,
	;
	private static final Set<LoginResult> successes = new HashSet<>(Arrays.asList(
			LoginResult.Success, LoginResult.AlreadyLoggedIn, LoginResult.ChangePassword));

	public static boolean isSuccess(LoginResult result) {
		return result != null && successes.contains(result);
	}

	public boolean isSuccess() {
		return successes.contains(this);
	}
}
