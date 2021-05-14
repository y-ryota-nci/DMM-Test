package jp.co.nci.iwf.endpoint.vd.vd0144;

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
 * パーツ計算式設定Endpoint.
 */
@Endpoint
@Path("/vd0144")
@RequiredLogin
@WriteAccessLog
public class Vd0144Endpoint extends BaseEndpoint<Vd0144Request> {

	@Inject
	private Vd0144Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0144Response init(Vd0144Request req) {
		return service.init(req);
	}

	/**
	 * 計算式の検証.
	 * @param req
	 * @return
	 */
	@POST
	@Path("/verify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0144Response verify(Vd0144Request req) {
		return service.verify(req);
	}
}
