package jp.co.nci.iwf.endpoint.unsecure.reset;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パスワードリセット画面Endpoint
 */
@Path("/resetPassword")
@Endpoint
@WriteAccessLog
public class ResetPasswordEndpoint extends BaseEndpoint<ResetPasswordRequest> {
	@Inject private ResetPasswordService service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(ResetPasswordRequest req) {
		return service.resetPassword(req);
	}
}
