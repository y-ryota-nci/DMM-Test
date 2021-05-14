package jp.co.nci.iwf.endpoint.dc.dc0120;

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
 * 業務文書メニュー編集Endpoint
 */
@Path("/dc0120")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0120Endpoint extends BaseEndpoint<Dc0120InitRequest> {

	@Inject
	private Dc0120Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0120InitResponse init(Dc0120InitRequest req) {
		return service.init(req);
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0120SaveResponse save(Dc0120SaveRequest req) {
		return service.save(req);
	}

	@POST
	@Path("/move")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0120MoveResponse move(Dc0120MoveRequest req) {
		return service.move(req);
	}

	@POST
	@Path("/associate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0120AssociateResponse associate(Dc0120AssociateRequest req) {
		return service.associate(req);
	}

}
