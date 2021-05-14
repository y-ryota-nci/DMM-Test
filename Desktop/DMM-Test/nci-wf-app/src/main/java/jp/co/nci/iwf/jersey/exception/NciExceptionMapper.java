package jp.co.nci.iwf.jersey.exception;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.component.i18n.JsonMessage;
import jp.co.nci.iwf.component.i18n.MessageCdHolder;
import jp.co.nci.iwf.jersey.base.BaseExceptionMapper;

/**
 * 汎用例外マッパー
 */
@Provider
@ApplicationScoped
public class NciExceptionMapper
		extends BaseExceptionMapper
		implements ExceptionMapper<Exception> {

	/** 多言語サービス */
	@Inject private I18nService i18n;
	/** アクセスログサービス */
	@Inject private AccessLogService accessLog;
	/** リクエストで要求されている言語対応用メッセージCD保持クラス */
	@Inject private MessageCdHolder messageCdHolder;

	/** クライアントへのエラー通知JSONフィールド名 */
	private static final String ALERTS = "alerts";


	@Override
	public Response toResponse(Exception exception) {
		// Reflection上のエラーではなく、直接のエラー原因を求める
		final Throwable e = toCause(exception);
		final Map<String, Object> entity = createExceptionEntity(e);

		// 例外内容をアクセスログへ記録
		try {
			accessLog.appendException(e);
		} catch (Exception ex) {
			log.warn(ex.getMessage(), ex);
		}

		// リクエストされたメッセージCDに対応した多言語リソース
		final List<JsonMessage> messages = messageCdHolder.getRequestedMessage();
		entity.put("messages", messages);

		// 排他エラー
		if ((e instanceof AlreadyUpdatedException)
				|| (e instanceof javax.persistence.OptimisticLockException)
				|| (e instanceof org.eclipse.persistence.exceptions.OptimisticLockException)) {
			return toResponseAlreadyUpdated(e, entity);
		}
//		// クライアントとサーバ間でユーザ情報が異なるエラー
//		if (e instanceof LoginUserWasSwitchedException) {
//			return toResponseLoginUserWasSwitched((LoginUserWasSwitchedException)e, entity);
//		}
		// 前提条件誤りエラー
		if (e instanceof PreconditionException) {
			// システムエラー画面へ遷移するのではなく、表示前の画面のままでエラー内容を表示する
			// 	・InvalidUserInputException
			// 	・InvalidTableDefinisionException
			return toResponsePreconditionFailed((PreconditionException)e, entity);
		}
		// WF APIエラー
		if (e instanceof WfException) {
			return toResponseWfException((WfException)e, entity);
		}
		// 認証エラー／セッションタイムアウトエラー
		if (e instanceof NotAuthorizedException) {
			return toResponseNotAuthorized((NotAuthorizedException)e, entity);
		}
		// Webアプリ汎用エラー
		if (e instanceof WebApplicationException)
			return toResponse((WebApplicationException)e, entity);

		return getJsonResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode())
				.entity(entity)
				.build();
	}

	/** Reflection上のエラーではなく、直接のエラー原因を求める */
	private Throwable toCause(Throwable e) {
		Throwable ex = e;
		while ((ex instanceof UndeclaredThrowableException || ex instanceof ReflectiveOperationException)
				&& ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

	@SuppressWarnings("unchecked")
	private void addAlerts(Map<String, Object> entity, String key, String msg) {
		List<String> values = (List<String>)entity.get(key);
		if (values == null) {
			values = new ArrayList<>();
			entity.put(key, values);
		}
		values.add(msg);
	}

	/** WF API上のエラー */
	private Response toResponseWfException(WfException e, Map<String, Object> entity) {
		// WF API側で排他エラーか？
		if (CodeMaster.ReturnCode.EXCLUSION.equals(e.getReturnCode())) {
			return toResponseAlreadyUpdated(e, entity);
		}

		addAlerts(entity, ALERTS, defaults(e.getMessage(), e.getMessageExt()));
		return getJsonResponse(Status.PRECONDITION_FAILED.getStatusCode())		// 500 Internal Server Error
				.type(MediaType.APPLICATION_JSON_TYPE)
				.entity(entity)
				.build();
	}

	/** 排他エラー */
	private Response toResponseAlreadyUpdated(Throwable e, Map<String, Object> entity) {
		addAlerts(entity, ALERTS, i18n.getText(MessageCd.MSG0050));

		return getJsonResponse(Status.CONFLICT.getStatusCode())		// 409 Conflict
				.entity(entity)
				.build();
	}

	/** 前提条件誤りエラー */
	private Response toResponsePreconditionFailed(PreconditionException e, Map<String, Object> entity) {
		addAlerts(entity, ALERTS, e.getLocalizedMessage());
		return getJsonResponse(Status.PRECONDITION_FAILED.getStatusCode())		// 412 Precondition Failed （前提条件誤り）
				.entity(entity)
				.build();
	}

	/** 認証エラー／セッションタイムアウトエラー */
	private Response toResponseNotAuthorized(NotAuthorizedException e, Map<String, Object> entity) {
		entity.put("redirectUrl", AppURL.NOT_AUTHENTICATED);

		return getJsonResponse(Status.UNAUTHORIZED.getStatusCode())		// 401 Unauthenticated 認証エラー
				.entity(entity)
				.build();
	}

	/** Webアプリの汎用例外 */
	private Response toResponse(WebApplicationException e, Map<String, Object> entity) {
		return getJsonResponse(e.getResponse().getStatus())
				.entity(entity)
				.build();
	}
}
