package jp.co.nci.iwf.component.authenticate;

import java.lang.reflect.Modifier;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import jp.co.nci.iwf.component.accesslog.AccessLogEndpointInterceptor;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.exception.LoginUserWasSwitchedException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * Endpointへのアクセスしたときにクライアントとサーバ間でログイン者情報が
 * 同じことを確認するためのインターセプター
 */
@Interceptor
@Dependent
@Priority(AccessLogEndpointInterceptor.PRIORITY)
@WriteAccessLog
public class AuthenticateEndpointInterceptor {
	@Inject private SessionHolder sessionHolder;

	/**
	 * クライアントとサーバ間でログイン者情報が同一化をチェック
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@AroundInvoke
	public Object validateSameLoginUser(InvocationContext ctx) throws Exception {
		if (Modifier.isPublic(ctx.getMethod().getModifiers())) {
			final LoginInfo login = sessionHolder.getLoginInfo();
			for (Object p : ctx.getParameters()) {
				// クライアント側の企業コード／ユーザコードがあるか
				String clientCorporationCode = null;
				String clientUserCode = null;
				if (p instanceof BaseRequest) {
					final BaseRequest req = (BaseRequest)p;
					clientCorporationCode = req.clientCorporationCode;
					clientUserCode = req.clientUserCode;
				}
				else if (p instanceof Map) {
					final Map map = (Map)p;
					clientCorporationCode = (String)map.get("clientCorporationCode");
					clientUserCode = (String)map.get("clientUserCode");
				}

				// クライアントから送信されてきたログイン者情報とセッション上のログイン者情報は同一か
				if (isNotEmpty(clientCorporationCode) && isNotEmpty(clientUserCode)) {
					if (!eq(login.getCorporationCode(), clientCorporationCode)
							|| !eq(login.getUserCode(), clientUserCode)) {
						throw new LoginUserWasSwitchedException(clientCorporationCode, clientUserCode, login);
					}
					// 一度でも判定がなされれば、同一リクエスト内でそれ以上は不要
					break;
				}
			};
		}
		// インターセプト元メソッド実行
		return ctx.proceed();
	}

	/** 非空判定 */
	private static boolean isNotEmpty(String s) {
		return MiscUtils.isNotEmpty(s);
	}

	/** 等価判定 */
	private static <T>  boolean eq(T t1, T t2) {
		return MiscUtils.eq(t1, t2);
	}
}
