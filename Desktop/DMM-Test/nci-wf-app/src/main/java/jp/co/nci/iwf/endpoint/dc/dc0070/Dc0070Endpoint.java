package jp.co.nci.iwf.endpoint.dc.dc0070;

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
 * テンプレート一覧Endpoint
 */
@Path("/dc0070")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0070Endpoint extends BaseEndpoint<Dc0070Request> {

	@Inject
	private Dc0070Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0070Response init(Dc0070Request req) {
		return service.init(req);
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0070Response search(Dc0070Request req) {
		return service.search(req);
	}

}
