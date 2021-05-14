package jp.co.nci.iwf.endpoint.gd.gd0010;

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
 * ガジェット(件数)画面Endpoint
 */
@Path("/gd0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Gd0010Endpoint extends BaseEndpoint<Gd0010Request> {

	@Inject private Gd0010Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Gd0010Response init(Gd0010Request req) {
		return service.init(req);
	}

}
