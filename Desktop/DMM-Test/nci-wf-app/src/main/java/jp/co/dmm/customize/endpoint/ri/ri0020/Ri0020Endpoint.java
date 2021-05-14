package jp.co.dmm.customize.endpoint.ri.ri0020;

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
 * 検収明細分割Endpoint
 */
@Path("/ri0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ri0020Endpoint extends BaseEndpoint<Ri0020Request> {

	@Inject private Ri0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ri0020Response init(Ri0020Request req) {
		return service.init(req);
	}
}
