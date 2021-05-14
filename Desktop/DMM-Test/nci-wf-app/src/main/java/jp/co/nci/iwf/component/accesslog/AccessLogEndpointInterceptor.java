package jp.co.nci.iwf.component.accesslog;

import java.lang.reflect.Modifier;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import jp.co.nci.iwf.component.i18n.MessageCdHolder;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * Endpointへのアクセスを記録するためのインターセプター
 */
@Interceptor
@Dependent
@Priority(AccessLogEndpointInterceptor.PRIORITY)
@WriteAccessLog
public class AccessLogEndpointInterceptor {
	@Inject
	private AccessLogService accessLog;
	@Inject
	private Logger log;
	@Inject
	private HttpServletRequest req;
	@Inject
	private MessageCdHolder holder;

	/** アクセスログIntercetorの優先順位 */
	public static final int PRIORITY = Interceptor.Priority.APPLICATION;

	@AroundInvoke
	public Object logging(InvocationContext ctx) throws Exception {
		// リクエストされたメッセージCDを保存
		// クリア処理がないけれど、別に残っていても何の問題もないから放置しよう。
		saveMessageCd(ctx.getParameters());

		if (Modifier.isPublic(ctx.getMethod().getModifiers())) {
			try {
				// アクセスログを記録し、後続処理用にそのアクセスログIDをスレッドローカルへ保存
				final Long accessLogId = accessLog.writeEntry(req);

				// ここで記録されるのは Endpointのパラメータ
				accessLog.appendDetail(accessLogId, ctx.getParameters());
			}
			catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}

		// インターセプト元メソッド実行
		return ctx.proceed();
	}

	/** リクエストされたメッセージCDを保存 */
	private void saveMessageCd(Object[] parameters) {
		for (Object p : parameters) {
			if (p instanceof BaseRequest) {
				final BaseRequest req = (BaseRequest)p;
				holder.saveThreadLocal(req);
			}
		}
	}
}
