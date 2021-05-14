package jp.co.nci.iwf.endpoint.vd.vd0112;

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
 * 有効条件設定Endpoint.
 */
@Endpoint
@Path("/vd0112")
@RequiredLogin
@WriteAccessLog
public class Vd0112Endpoint extends BaseEndpoint<Vd0112Request> {

	@Inject
	private Vd0112Service service;

	/**
	 * 初期化.
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0112Response init(Vd0112Request req) {
		return service.init(req);
	}

	/**
	 * 有効条件の評価式の検証.
	 */
	@POST
	@Path("/verify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0112Response verify(Vd0112Request req) {
		return service.verify(req);
	}
}
