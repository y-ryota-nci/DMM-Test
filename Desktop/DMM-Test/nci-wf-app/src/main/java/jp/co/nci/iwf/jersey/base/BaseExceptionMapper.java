package jp.co.nci.iwf.jersey.base;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 例外マッパーの基底クラス
 */
@ApplicationScoped
public abstract class BaseExceptionMapper extends MiscUtils implements CodeBook {
	@Context
	protected HttpServletRequest req;
	@Inject
	protected SessionHolder sessionHolder;
	@Inject
	protected Logger log;

	/**
	 * 例外情報を保持したエンティティを生成
	 * @param e
	 * @return
	 */
	protected Map<String, Object> createExceptionEntity(Throwable e) {
		final LoginInfo login = (sessionHolder != null ? sessionHolder.getLoginInfo() : null);
		final Map<String, Object> m = new HashMap<>();
		m.put("requestedURL", getURL(req));
		m.put("serverException", e.toString());
		m.put("timestamp", MiscUtils.FORMATTER_DATETIME.get().format(now()));
		if (login == null) {
			m.put("topPageUrl", AppURL.LOGIN);
		}
		else {
			m.put("loginInfo", login);
			m.put("topPageUrl", login.getTopPageUrl());
			m.put("corporationCode", login.getCorporationCode());
			m.put("userAddedInfo", login.getUserAddedInfo());
			m.put("userName", login.getUserName());
		}

		String msg = e.getMessage();
		if (e instanceof WfException) {
			String msgEx = ((WfException)e).getMessageExt();
			if (isNotEmpty(msgEx))
				msg += " " + msgEx;
		}
		msg += " " + getURL(req);
		log.error(msg, e);

		return m;
	}

	/** JSON用レスポンスを生成 */
	protected ResponseBuilder getJsonResponse(int status) {
		return Response
				.status(status)
				.type("application/json; charset=utf-8");	// Chrome57以降、JSONをそのまま返すと日本語が文字化けする
	}
}
