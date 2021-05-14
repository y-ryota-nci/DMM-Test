package jp.co.nci.iwf.endpoint.dc.dc0030;

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

@Path("/dc0030")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0030Endpoint extends BaseEndpoint<Dc0030InitRequest> {

	@Inject
	private Dc0030Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0030InitResponse init(Dc0030InitRequest req) {
		return service.init(req);
	}

}
