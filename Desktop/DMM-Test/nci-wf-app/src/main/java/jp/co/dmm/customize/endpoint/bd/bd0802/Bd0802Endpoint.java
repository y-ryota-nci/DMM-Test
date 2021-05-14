package jp.co.dmm.customize.endpoint.bd.bd0802;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算計画履歴作成画面Endpoint
 */
@Path("/bd0802")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0802Endpoint extends BaseEndpoint<Bd0802InitRequest> {
	@Inject private Bd0802Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Bd0802InitRequest req) {
		return service.init(req);
	}

	/**
	 * 履歴作成
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse create(Bd0802CreateRequest req) {
		return service.create(req);
	}

}
