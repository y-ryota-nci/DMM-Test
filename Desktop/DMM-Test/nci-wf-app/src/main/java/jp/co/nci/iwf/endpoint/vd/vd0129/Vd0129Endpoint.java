package jp.co.nci.iwf.endpoint.vd.vd0129;

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
 * DBカラム名一覧Endpoint
 */
@Path("/vd0129")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0129Endpoint extends BaseEndpoint<Vd0129Request> {
	@Inject
	private Vd0129Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0129Response init(Vd0129Request req) {
		return service.init(req);
	}

}
