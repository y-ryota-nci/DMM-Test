package jp.co.nci.iwf.endpoint.vd.vd0170;

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
 * 承認状況画面Endpoint
 */
@Path("/vd0170")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0170Endpoint extends BaseEndpoint<Vd0170Request> {
	@Inject
	private Vd0170Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0170Response init(Vd0170Request req) {
		return service.init(req);
	}
}
