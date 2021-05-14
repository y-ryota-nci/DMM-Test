package jp.co.nci.iwf.endpoint.na.na0000;

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
 * 新規申請フォルダ設定Endpoint
 */
@Path("/na0000")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Na0000Endpoint extends BaseEndpoint<Na0000InitRequest> {

	@Inject
	private Na0000Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Na0000InitResponse init(Na0000InitRequest req) {
		return service.init(req);
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0000SaveResponse save(Na0000SaveRequest req) {
		return service.save(req);
	}

	@POST
	@Path("/move")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0000MoveResponse move(Na0000MoveRequest req) {
		return service.move(req);
	}

	@POST
	@Path("/associate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0000AssociateResponse associate(Na0000AssociateRequest req) {
		return service.associate(req);
	}

}
