package jp.co.nci.iwf.endpoint.dc.dc0022;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.tray.DocTrayResponse;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 詳細検索Endpoint
 */
@Path("/dc0022")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0022Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject
	private Dc0022Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public TrayInitResponse init(BaseRequest req) {
		return service.init(req);
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DocTrayResponse search(TraySearchRequest req) {
		return service.search(req);
	}

}
