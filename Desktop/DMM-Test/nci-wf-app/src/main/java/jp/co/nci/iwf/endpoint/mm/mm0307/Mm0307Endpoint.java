package jp.co.nci.iwf.endpoint.mm.mm0307;

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
 * アクション定義作成Endpoint
 */
@Path("/mm0307")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0307Endpoint extends BaseEndpoint<Mm0307Request>{

	@Inject
	private Mm0307Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(Mm0307Request req) {
		return service.init(req);
	}

	/**
	 * 作成
	 * @param req
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse create(Mm0307Request req) {
		return service.create(req);
	}

}
