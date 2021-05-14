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
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.system.CorporationPropertyService;

/**
 * LDAPによるユーザ認証サービス。
 * ログイン画面から得た内容でLDAPサーバへ問合せし、その認証結果をもとにログイン処理を行う
 */
@BizLogic
@Named(AuthenticateService.LDAP)
public class AuthenticateServiceLDAP extends AuthenticateServiceBase {
	/** 企業プロパティ・サービス  */
	@Inject private CorporationPropertyService prop;
	@Inject private Logger log;
	@Inject private AccessLogService accessLog;


	/** LDAPサーバへ接続するためのパラメータの元 */
	private final Hashtable<String, String> base = new Hashtable<>();
	/** LDAPサーバへのURL */
	private String providerUrl;
	/** 認証識別子の末尾詞 */
	private String principalSuffix;

	/**
	 * 初期化
	 */
	@PostConstruct
	public void init() {
		// 環境変数の読み込み
		// 普通は CorporationPropertyServiceから取得する
	    providerUrl = prop.getString(CorporationProperty.LDAP_PROVIDER_URL);	//"ldap://192.168.33.10:10389/";
		principalSuffix = prop.getString(CorporationProperty.LDAP_PRINCIPAL_SUFFIX);	//";ou=system";

		base.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		base.put(Context.SECURITY_AUTHENTICATION, "simple");
		base.put(Context.PROVIDER_URL, providerUrl); //LDAPサーバ
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
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// LDAP認証では trayLogin(String, String, String)を使うこと
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
		// ASP管理者はLDAPサーバに存在しないので、DBベースで認証する
		if (eq(CorporationCodes.ASP, corporationCode) && eq(UserAddedInfos.ADMIN, userAddedInfo)) {
			AuthenticateService authDB = AuthenticateService.get(AuthenticateService.DB);
			LoginResult r = authDB.tryLogin(corporationCode, userAddedInfo, plainPassword);
			final Long accessLogId = accessLog.loadAccessLogId();
			if (accessLogId != null) {
				accessLog.updateResult(accessLogId, LoginResult.isSuccess(r));	// AccessLogFilterより先に処理結果を書き込むことで、結果を確定させてしまう
				accessLog.appendDetail(accessLogId, toAccessLogDetailMap(r, null));
			}
			return r;
		}

		Context ctx = null;
		try {
			// LDAPサーバへ接続し、指定ユーザへの認証を行う
			final Hashtable<String, String> env = new Hashtable<>(base);
			env.put(Context.SECURITY_PRINCIPAL, "uid=" + userAddedInfo + principalSuffix);
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
