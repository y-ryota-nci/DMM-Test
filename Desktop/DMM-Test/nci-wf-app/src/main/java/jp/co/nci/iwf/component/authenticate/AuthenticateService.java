package jp.co.nci.iwf.component.authenticate;

import java.util.Set;

import javax.enterprise.inject.literal.NamedLiteral;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;

import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ユーザ認証サービス
 */
public interface AuthenticateService {
	// @Namedの仕様的に、クラス名の先頭を小文字にせねばならぬ
	/** 標準認証（ローカルDBへ認証可否を問い合わせる） */
	String DB = "authenticateServiceDB";
	/** LDAP認証（汎用LDAPサーバへ認証可否を問い合わせる） */
	String LDAP = "authenticateServiceLDAP";
	/** LDAP認証（ActiveDirectoryをLDAPサーバとして認証可否を問い合わせる） */
	String AD = "authenticateServiceAD";
	/** 統合Windows認証（Integrated Windows authentication） */
	String IWA = "authenticateServiceIWA";

	/**
	 * システムプロパティで指定された認証方法に対応した認証サービスを返す
	 * @return
	 */
	static AuthenticateService get() {
		final CorporationPropertyService prop = CDI.current().select(CorporationPropertyService.class).get();
		final String m = prop.getString(CorporationProperty.AUTHENTICATION_METHOD);
		String authMethod = DB;
		if ("DB".equals(m))
			authMethod = DB;
		else if ("AD".equals(m))
			authMethod = AD;
		else if ("IWA".equals(m))
			authMethod = IWA;
		else if ("LDAP".equals(m))
			authMethod = LDAP;

		return get(authMethod);
	}

	/**
	 * 認証方法に対応した認証サービスを返す
	 * @param authMethod 認証方法
	 * @return
	 */
	static AuthenticateService get(String authMethod) {
		final Set<String> enables = MiscUtils.asSet(DB, LDAP, AD, IWA);
		if (!enables.contains(authMethod))
			throw new InternalServerErrorException("許可されない認証方法が指定されています authType=" + authMethod);

		final NamedLiteral selector = NamedLiteral.of(authMethod);
		return CDI.current().select(AuthenticateService.class, selector).get();
	}

	/**
	 * ログイン認証結果をもとに、セッション情報を書き換え
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @param corporationCode 企業コード
	 * @param userAddedInfo ログインID
	 * @return
	 */
	public LoginInfo updateSessionIfSuccess(LoginResult result, String newLocaleCode, String corporationCode, String userAddedInfo);

	/**
	 * ログイン認証結果をもとに、セッション情報を書き換え【SSO認証トークンをHTTPリクエストから吸上げて認証】
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @return
	 */
	public LoginInfo updateSessionIfSuccess(LoginResult result, String newLocaleCode);

	/**
	 * なりすまし先ユーザ情報でセッション情報を書き換え
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @param corporationCode 企業コード
	 * @param userAddedInfo ログインID
	 * @return 新しいログイン情報
	 */
	public LoginInfo updateSessionBySpoofing(String corporationCode, String userAddedInfo);

	/**
	 * ログイン済みか
	 * @return
	 */
	public boolean isAuthenticated();

	/**
	 * ログイン可能かを判定【SSO認証トークンをHTTPリクエストから吸上げて認証】
	 * @return 認証結果
	 */
	public LoginResult tryLogin();

	/**
	 * ログイン可能かを判定【パスワード判定無し】
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 認証結果
	 */
	public LoginResult tryLogin(String corporationCode, String userAddedInfo);

	/**
	 * ログイン可能かを判定
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @param plainPassword パスワード（平文）
	 * @return 認証結果
	 */
	public LoginResult tryLogin(String corporationCode, String userAddedInfo, String plainPassword);

	/**
	 * 「ログイン前に初回リクエストされたURI」を返す。
	 * @return
	 */
	public String getFirstRequestURL();

	/**
	 * 現在のリクエストURIを「ログイン前に初回リクエストされたURI」として保存
	 */
	public void saveFirstRequestURL(HttpServletRequest req);

	/**
	 * 現在のリクエストURIを「ログイン前に初回リクエストされたURI」として保存
	 * （Endpointに対するリクエストなので、refererから元のURIを求める）
	 */
	public void saveFirstRequestURL(ContainerRequestContext crc);

	/** ログイン画面を使うか */
	public boolean isUseLoginScreen();

	/** シングルサインオン（SSO)認証を使うか */
	public boolean isEnableSSO();

	/**
	 * ログイン処理結果に応じたメッセージを返す
	 * @param result ログイン処理結果
	 * @return
	 */
	public String getLoginResultMessage(LoginResult result);
}
