package jp.co.nci.iwf.component.openid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * OpenID Connect
 */
@RequestScoped
@Named("openIdConnect")
public class OpenIdConnect {

	private static final String KEYSTORE_JSON = "/key/oidc_keystore.json";

	private static final String OPEN_ID_CONNECT_REDIRECT = "OpenID Connect 対象画面へ遷移します=";
	private static final String OPEN_ID_CONNECT_REDIRECT_URL_ERROR = "OpenId Connect リダイレクトアドレスに異常があります ";
	private static final String OPEN_ID_CONNECT_TOKEN_ERROR = "OpenId Connect トークンに異常があります";
	private static final String OPEN_ID_CONNECT_WELLKNOWN_ERROR = "OpenId Connect well-knownに異常があります";
	private static final String OPEN_ID_CONNECT_KEYSTORE_ERROR = "OpenId Connect keystore.jsonに異常があります";
	private static final String OPEN_ID_CONNECT_JKWS_URI_ERROR = "OpenId Connect jwks_uriに異常があります";

	public static ConcurrentHashMap<String, Map<String, String>> OpenIdConnectMap = new ConcurrentHashMap<String, Map<String, String>>(); ;

	public static final String OPEN_ID = "openid";
	public static final String OPEN_ID_RESPONSE_TYPE = "response_type";
	public static final String OPEN_ID_CLIENT_ID = "client_id";
	public static final String OPEN_ID_CLIENT_SECRET = "client_secret";
	public static final String OPEN_ID_REDIRECT_URI = "redirect_uri";
	public static final String OPEN_ID_STATE = "state";
	public static final String OPEN_ID_SCOPE = "scope";
	public static final String OPEN_ID_CODE = "code";
	public static final String GRANT_TYPE = "grant_type";
	public static final String OPEN_ID_NONCE = "nonce";
	public static final String OPEN_ID_AUTHORIZATION_CODE = "authorization_code";
	public static final String OPEN_ID_DISPLAY = "display";
	public static final String OPEN_ID_AUTHENTICATION = "authentication";
	public static final String OPEN_ID_CONNECT_PARAM = "OpenIdConnectParam";
	public static final String OPEN_ID_ACCESS_TOKEN = "OpenIdAccessToken";
	public static final String OPEN_ID_USER_ID = "OpenIdUserId";

	public static final Long OPEN_ID_ERROR_EXPIRE = 3600L * 8;
	public static final String CREATE_TIMESTAMP = "CreateTimeStamp";


	private Logger log = LoggerFactory.getLogger(OpenIdConnect.class);

	@Inject
	protected SessionHolder sessionHolder;

	/**
	 * 設定情報レスポンス
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 */
	public void wellKnownResponse(HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> json = new HashMap<String, Object>();
		String contextName = request.getRequestURL().toString().split(request.getContextPath())[0];
		if (isHttps(request)) {
			contextName = contextName.replace("http://", "https://");
		}

		json.put("issuer", contextName);
		json.put("authorization_endpoint", contextName + request.getContextPath() + "/oidc/auth");
		json.put("token_endpoint", contextName + request.getContextPath() + "/oidc/token");
		json.put("jwks_uri", contextName + request.getContextPath() + "/oidc/certs");

		json.put("grant_types_supported", new String[] {"authorization_code"});
		json.put("response_types_supported", new String[] {"code"});
		json.put("id_token_signing_alg_values_supported", new String[] {"RS256"});
		json.put("scopes_supported", new String[] {"openid"});
		json.put("token_endpoint_auth_methods_supported", new String[] {"client_secret_basic"});
		json.put("claims_supported", new String[] {"openid"});

		String tokenJson = MiscUtils.toJsonFromObj(json);

		try {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().println(tokenJson);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			log.error(OPEN_ID_CONNECT_WELLKNOWN_ERROR);
		}
	}

	/**
	 * パラメータ取得
	 * @param request HTTPリクエスト
	 */
	public Map<String, String> getOpenIdConnectParam(HttpServletRequest request) {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(OPEN_ID_RESPONSE_TYPE, request.getParameter(OPEN_ID_RESPONSE_TYPE));
		map.put(OPEN_ID_CLIENT_ID, request.getParameter(OPEN_ID_CLIENT_ID));
		map.put(OPEN_ID_REDIRECT_URI, request.getParameter(OPEN_ID_REDIRECT_URI));
		map.put(OPEN_ID_STATE, request.getParameter(OPEN_ID_STATE));
		map.put(OPEN_ID_SCOPE, request.getParameter(OPEN_ID_SCOPE));
		map.put(OPEN_ID_STATE, request.getParameter(OPEN_ID_STATE));
		map.put(OPEN_ID_NONCE, request.getParameter(OPEN_ID_NONCE));
		map.put(OPEN_ID_DISPLAY, request.getParameter(OPEN_ID_DISPLAY));

		return map;
	}

	/**
 	 * 認証レスポンス
	 * @param openIdConnectParam パラメータ
	 * @param response HTTPレスポンス
	 */
	public String  AuthenticationResponse(Map<String, String> openIdConnectParam, String userId, boolean https, HttpServletResponse response) {
		String redirect_uri = openIdConnectParam.get(OPEN_ID_REDIRECT_URI);

		log.info(OPEN_ID_CONNECT_REDIRECT + redirect_uri);
        StringBuilder url = new StringBuilder();
        url.append(redirect_uri);

        UUID code = UUID.randomUUID();

        url.append("?code=").append(code.toString());
        url.append("&state=").append(openIdConnectParam.get(OPEN_ID_STATE));

	    long unixTimestamp = Instant.now().getEpochSecond();
		OpenIdConnectMap.compute(code.toString(), (k, v) ->  {
			openIdConnectParam.put(OPEN_ID_CODE, code.toString());
			openIdConnectParam.put(OPEN_ID_USER_ID, userId);
			openIdConnectParam.put(CREATE_TIMESTAMP, String.valueOf(unixTimestamp));
			log.debug(openIdConnectParam.toString());
			return openIdConnectParam;
		});

		removeErrorKey(code, unixTimestamp);

		try {
			String urlStr = response.encodeRedirectURL(url.toString());
			if (https) {
				urlStr = urlStr.replace("http://", "https://");
			}
			response.sendRedirect(urlStr);
		} catch (IOException e) {
			log.error(OPEN_ID_CONNECT_REDIRECT_URL_ERROR + url.toString());
		}

		return code.toString();
	}

	/**
	 * 一定時間後に残っている情報を削除
	 * @param code コード
	 * @param unixTimestamp　現在時刻
	 */
	private void removeErrorKey(UUID code, long unixTimestamp) {
		List<String> removeKey = new ArrayList<String>();
		for (String key : OpenIdConnectMap.keySet()) {
			if (code.toString().equals(key)) {
				continue;
			}

			String createTimestamp = OpenIdConnectMap.get(key).get(CREATE_TIMESTAMP);
			if (createTimestamp != null) {
				if ((Long.parseLong(createTimestamp) + OPEN_ID_ERROR_EXPIRE) < unixTimestamp) {
					removeKey.add(key);
					continue;
				}
			}
		}

		for (String key : removeKey) {
			OpenIdConnectMap.remove(key);
		}
	}

	/**
	 * トークンレスポンス
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 */
	public void tokenResponse(HttpServletRequest request, HttpServletResponse response) {

		String redirect_uri = request.getParameter(OPEN_ID_REDIRECT_URI);
		log.info(OPEN_ID_CONNECT_REDIRECT + redirect_uri);
		StringBuilder url = new StringBuilder();
		url.append(redirect_uri);

		try (FileInputStream stream = new FileInputStream(new File(getClass().getResource(KEYSTORE_JSON).getPath()))) {

			JWKSet localKeys = JWKSet.load(stream);
		    long unixTimestamp = Instant.now().getEpochSecond();

		    Map<String, String> map = OpenIdConnectMap.get(request.getParameter(OPEN_ID_CODE));

			JWTClaimsSet payload = new JWTClaimsSet.Builder()
		      .claim("iss", request.getRequestURL().toString().split(request.getContextPath())[0])
		      .claim("sub", map.get(OPEN_ID_USER_ID))
		      .claim("aud", map.get(OPEN_ID_CLIENT_ID))
		      .claim("nonce", map.get(OPEN_ID_NONCE))
		      .claim("exp", unixTimestamp + OPEN_ID_ERROR_EXPIRE)
		      .claim("auth_time", unixTimestamp)
		      .claim("iat", unixTimestamp)
		      .build();

		    SignedJWT signedJWT;

			JSONObject keystore =  localKeys.toJSONObject(false);
			JSONArray keyarray = (JSONArray) keystore.get("keys");

			RSAKey key = RSAKey.parse((JSONObject) keyarray.get(0));

			String keyID = localKeys.getKeys().get(0).getKeyID();
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
			  .keyID(keyID)
			  .type(JOSEObjectType.JWT)
			  .build();

		    JWSSigner signer = new RSASSASigner(key.toRSAPrivateKey());
			signedJWT = new SignedJWT(header, payload);
			signedJWT.sign(signer);

		    UUID accessToken = UUID.randomUUID();
		    map.put(OPEN_ID_ACCESS_TOKEN, accessToken.toString());

		    Map<String, String> json = new HashMap<String, String>();
			json.put("access_token", accessToken.toString());
			json.put("token_type", "bearer");
			json.put("expires_in", OPEN_ID_ERROR_EXPIRE.toString());
			json.put("id_token", signedJWT.serialize());
			String tokenJson = MiscUtils.toJsonFromObj(json);

			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().println(tokenJson);
			response.getWriter().flush();
			response.getWriter().close();

		} catch (JOSEException e) {
			log.error(OPEN_ID_CONNECT_TOKEN_ERROR);
		} catch (IOException e1) {
			log.error(OPEN_ID_CONNECT_KEYSTORE_ERROR);
		} catch (ParseException e1) {
			log.error(OPEN_ID_CONNECT_KEYSTORE_ERROR);
		}
	}

	/**
	 * 　認証要求か？
	 * @param request HTTPリクエスト
	 * @return 認証要求
	 */
	public boolean isAuthenticationRequest(Map<String, String> map) {

		if (map == null) {
			return false;
		}

		String response_type = map.get(OPEN_ID_RESPONSE_TYPE);
		String client_id = map.get(OPEN_ID_CLIENT_ID);
		String redirect_uri = map.get(OPEN_ID_REDIRECT_URI);
		String scope = map.get(OPEN_ID_SCOPE);
		if (StringUtils.isEmpty(response_type)) {
			return false;
		}
		if (StringUtils.isEmpty(client_id)) {
			return false;
		}
		if (StringUtils.isEmpty(redirect_uri)) {
			return false;
		}
		if (StringUtils.isEmpty(scope) || scope.indexOf(OPEN_ID) == -1) {
			return false;
		}
		return true;
	}

	/**
	 * 　トークン要求か？
	 * @param request HTTPリクエスト
	 * @return トークン要求
	 */
	public boolean isTokenRequest(HttpServletRequest request) {

		String code = request.getParameter(OPEN_ID_CODE);
		String grant_type = request.getParameter(GRANT_TYPE);
		String redirect_uri = request.getParameter(OPEN_ID_REDIRECT_URI);
		if (StringUtils.isEmpty(code)) {
			return false;
		}
		if (StringUtils.isEmpty(grant_type) || !OPEN_ID_AUTHORIZATION_CODE.equals(grant_type)) {
			return false;
		}
		if (StringUtils.isEmpty(redirect_uri)) {
			return false;
		}
		return true;
	}


	/**
	 * JKT レスポンス
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 */
	public void certsResponse(HttpServletRequest request, HttpServletResponse response) {
		try (FileInputStream stream = new FileInputStream(new File(getClass().getResource(KEYSTORE_JSON).getPath()))) {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");

			JWKSet localKeys = JWKSet.load(stream);
			response.getWriter().println(localKeys.toJSONObject(true).toJSONString());

		    response.getWriter().flush();
		    response.getWriter().close();
		} catch (IOException e) {
			log.error(OPEN_ID_CONNECT_JKWS_URI_ERROR);
		} catch (ParseException e) {
			log.error(OPEN_ID_CONNECT_JKWS_URI_ERROR);
		}
	}

	/**
	 * HTTPSか
	 * @param request HTTPリクエスト
	 * @return HTTPS
	 */
	public boolean isHttps(HttpServletRequest request) {
		if ("https".equals(request.getHeader("X-Forwarded-Proto"))) {
			return true;
		}
		return false;
	}

	public SessionHolder getSessionHolder() {
		return sessionHolder;
	}
}
