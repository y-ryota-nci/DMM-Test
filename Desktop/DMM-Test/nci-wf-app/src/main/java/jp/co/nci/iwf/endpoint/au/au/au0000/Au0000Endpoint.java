package jp.co.nci.iwf.endpoint.au.au.au0000;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * なりすまし先選択画面Endpoint
 *
 *
 */
@Endpoint
@Path("/au0000")
@RequiredLogin
@WriteAccessLog
public class Au0000Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Au0000Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Au0000Response init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * なりすましの実施
	 * @param req
	 * @return
	 */
	@Path("/spoof")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse spoof(Au0000Request req) {
		return service.spoof(req);
	}
}
