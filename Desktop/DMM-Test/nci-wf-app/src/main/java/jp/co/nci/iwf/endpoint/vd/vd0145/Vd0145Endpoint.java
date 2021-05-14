package jp.co.nci.iwf.endpoint.vd.vd0145;

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
 * パーツ計算条件設定Endpoint.
 */
@Endpoint
@Path("/vd0145")
@RequiredLogin
@WriteAccessLog
public class Vd0145Endpoint extends BaseEndpoint<Vd0145Request> {

	@Inject
	private Vd0145Service service;

	/**
	 * 初期化.
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0145Response init(Vd0145Request req) {
		return service.init(req);
	}

	/**
	 * 計算条件の評価式の検証.
	 */
	@POST
	@Path("/verify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0145Response verify(Vd0145Request req) {
		return service.verify(req);
	}
}
