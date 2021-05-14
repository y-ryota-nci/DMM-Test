package jp.co.nci.iwf.component;;

/**
 * 企業属性の定数(WFM_CORP_PROP_MASTER.PROPERTY_CODE)
 */
public enum CorporationProperty {
	/** ログイン失敗を許容する回数 */
	LOGIN_ALLOWED_FAILURE_COUNT,
	/** パスワード期限切れを警告する期間(日数) */
	PASSWORD_WARNING_TERM,
	/** パスワード有効期間(日数) */
	PASSWORD_VALIDITY_TERM,
	/** パスワードの最低文字数 */
	MIN_PASSWORD_LENGTH,
	/** パスワードに英大文字|英小文字|数字|記号から３種類以上の文字を混在させる */
	PASSWORD_COMPLEXITY,
	/** パスワードにログインＩＤを含めても良いか */
	ALLOWED_LOGINID_CONTAIN,
	/** 同一パスワードを禁止する世代数 */
	PASSWORD_VALIDITY_GENERATION,
	/** 初回ログイン時にパスワード変更を強制 */
	FIRST_LOGIN_CHANGE,
	/** 代理者設定時に通知メール送信 */
	AUTH_TRANSFER_NOTIFICATION,
	/** アカウントロック時に通知メール送信 */
	ACCOUNT_LOCK_NOTIFICATION,
	/** キャッシュ保持秒数(秒)。開発環境なら10、検証／本番環境は0を推奨 */
	CACHE_INTERVAL_SECONDS,
	/** サイト識別用の背景色 */
	SITE_BG_COLOR,
	/** サイト識別用のフォント色 */
	SITE_FONT_COLOR,
	/** 管理者にだけログインを許可する */
	LOGIN_FOR_ADMIN_ONLY,
	/** LDAP認証時のLDAPサーバURL */
	LDAP_PROVIDER_URL,
	/** LDAP認証時のPRINCIPALの末尾詞 */
	LDAP_PRINCIPAL_SUFFIX,
	/** ActiveDirectory認証でのドメイン名 */
	AD_DOMAIN_NAME,
	/** 認証方法(DB:通常、LDAP:LDAP認証、AD:ActiveDirectory認証、IWA:Windows統合認証) */
	AUTHENTICATION_METHOD,
	/** ログイン画面を使用する */
	USE_LOGIN_SCREEN,
	/** 管理者になりすましを許可する */
	ALLOW_ADMIN_TO_IMPERSONATE,
	/** ログイン画面にデバッグ情報を表示する */
	DISPLAY_DEBUG_INFO_ON_LOGIN,
	/** 検証環境用の背景画像を使う */
	USE_TEST_BG_IMAGE,
	/** 画面フッターにDB接続先と認証方法を表示する */
	DISPLAY_DEBUG_INFO_ON_FOOTER,
	/** 言語の切り替えを使用する */
	USE_LANGUAGE_SWITCHING,
	/** プロファイル情報アップロードの排他キー(非公開) */
	UPLOAD_PROFILE,
	/** 申請文書あたりのファイルサイズ上限(単位：MB)。0なら無制限 */
	MAX_FILE_SIZE_PER_PROCESS,
	/** アップロードあたりのファイルサイズ(単位：MB)。0なら無制限 */
	MAX_FILE_SIZE_PER_UPLOAD,
	/** 承認者設定画面を使用する */
	USE_APPROVERS_SETTING_SCREEN,
	/** ダッシュボードURL */
	DASHBOARD_URL,
	/** 旧消費税率(8%)を無効にする基準の日付 */
	OLD_TAX_RATE_DISPLAY_REFERENCE_DATE
	;
	public boolean equals(String s) {
		if (s == null) return false;
		return this.toString().equals(s);
	}
}
