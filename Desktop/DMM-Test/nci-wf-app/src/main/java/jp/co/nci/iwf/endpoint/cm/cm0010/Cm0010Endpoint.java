package jp.co.nci.iwf.endpoint.cm.cm0010;

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

/**
 * 企業選択画面Endpoint
 */
@Path("/cm0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Cm0010Endpoint extends BaseEndpoint<Cm0010Request> {

	@Inject private Cm0010Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cm0010Response init(Cm0010Request req) {
		return service.init(req);
	}


	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cm0010Response getWorklist(Cm0010Request req) {
		return service.search(req);
	}
}
