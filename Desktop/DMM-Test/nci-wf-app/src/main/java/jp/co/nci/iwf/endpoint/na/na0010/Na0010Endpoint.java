package jp.co.nci.iwf.endpoint.na.na0010;

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

@Path("/na0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Na0010Endpoint extends BaseEndpoint<Na0010InitRequest> {

	@Inject
	private Na0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Na0010InitResponse init(Na0010InitRequest req) {
		return service.init(req);
	}

}
