package jp.co.nci.iwf.endpoint.dc.dc0060;

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
 * 拡張項目一覧Endpoint
 */
@Path("/dc0060")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0060Endpoint extends BaseEndpoint<Dc0060Request> {

	@Inject
	private Dc0060Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0060Response init(Dc0060Request req) {
		return service.init(req);
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0060Response search(Dc0060Request req) {
		return service.search(req);
	}

}
