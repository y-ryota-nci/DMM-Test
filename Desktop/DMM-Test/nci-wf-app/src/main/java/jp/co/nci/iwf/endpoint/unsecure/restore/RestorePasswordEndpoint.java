package jp.co.nci.iwf.endpoint.unsecure.restore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パスワード復旧画面Endpoint
 */
@Path("/restorePassword")
@Endpoint
@WriteAccessLog
public class RestorePasswordEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject private RestorePasswordService service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 仮パスワード発行
	 * @param req
	 * @return
	 */
	@Path("/sendMail")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse sendMail(RestorePasswordRequest req) {
		return service.send(req);
	}
}
