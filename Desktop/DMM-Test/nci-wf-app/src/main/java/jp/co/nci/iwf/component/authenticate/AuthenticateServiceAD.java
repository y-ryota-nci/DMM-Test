package jp.co.nci.iwf.component.authenticate;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.system.CorporationPropertyService;

/**
 * ログイン画面から得た内容をもとに、ActiveDirectoryを認証サーバとしてLDAP認証を行う。
 *
 * 当クラスは AuthenticateServiceLDAP のActiveDirectory特化版である。
 * また、AuthenticateServiceIWA とは異なり、Windows統合認証には無関係である。
 * あくまで LDAPプロトコル経由でActiveDirectoryへ認証を行うだけ。
 */
@BizLogic
@Named(AuthenticateService.AD)
public class AuthenticateServiceAD extends AuthenticateServiceBase {
	/** 企業プロパティ・サービス  */
	@Inject private CorporationPropertyService prop;
	@Inject private Logger log;


	/** LDAPサーバへ接続するためのパラメータの元 */
	private final Hashtable<String, String> base = new Hashtable<>();

	/**
	 * 初期化
	 */
	@PostConstruct
	public void init() {
		base.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		base.put(Context.SECURITY_AUTHENTICATION, "simple");
	}

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
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// AD認証では trayLogin(String, String, String)を使うこと
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

		Context ctx = null;
		try {
			String providerURL = prop.getString(CorporationProperty.LDAP_PROVIDER_URL);
			String domainName = prop.getString(CorporationProperty.AD_DOMAIN_NAME);	//"nci.local";

			// LDAPサーバへ接続し、指定ユーザへの認証を行う
			final Hashtable<String, String> env = new Hashtable<>(base);
			env.put(Context.PROVIDER_URL, providerURL); //LDAPサーバ
			env.put(Context.SECURITY_PRINCIPAL, userAddedInfo + "@" + domainName);
			env.put(Context.SECURITY_CREDENTIALS, plainPassword);
			ctx = new InitialDirContext(env);

			// LDAP認証が成功なら、対象ユーザがユーザマスタ等に存在するかを検証
			return super.tryLogin(corporationCode, userAddedInfo);
		}
		catch (AuthenticationException e) {
			// LDAP認証エラー
			log.info(e.getMessage());
			return LoginResult.NotAuthenticatedLDAP;
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
		finally {
			if (ctx != null) {
				try { ctx.close(); }
				catch (NamingException ex) {}
			}
		}
	}
}
