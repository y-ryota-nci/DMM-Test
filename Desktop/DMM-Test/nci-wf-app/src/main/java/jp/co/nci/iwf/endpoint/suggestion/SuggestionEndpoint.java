package jp.co.nci.iwf.endpoint.suggestion;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.sandbox.SandboxSuggestionResponse;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * Suggestionサービス
 */
@Path("/suggestion")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class SuggestionEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject private SuggestionService service;

	@Override
	public BaseResponse init(BaseRequest req) {
		throw new BadRequestException("このメソッドはダミーなので呼び出せません");
	}

	/**
	 * ユーザ情報Suggestion
	 * @return
	 */
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SandboxSuggestionResponse suggestUser(SuggestionUserRequest req) {
		return service.suggestUser(req);
	}
}
