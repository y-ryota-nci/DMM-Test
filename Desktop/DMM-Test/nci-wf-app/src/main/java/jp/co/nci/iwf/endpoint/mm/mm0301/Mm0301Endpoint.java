package jp.co.nci.iwf.endpoint.mm.mm0301;

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
 * ルート作成Endpoint
 */
@Path("/mm0301")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0301Endpoint extends BaseEndpoint<Mm0301Request>{

	@Inject
	private Mm0301Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(Mm0301Request req) {
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
	public BaseResponse create(Mm0301Request req) {
		return service.create(req);
	}

}
