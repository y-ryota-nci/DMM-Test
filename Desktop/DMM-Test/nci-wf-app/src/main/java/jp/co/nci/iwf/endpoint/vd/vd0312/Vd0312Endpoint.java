package jp.co.nci.iwf.endpoint.vd.vd0312;

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
 * 承認ルート情報設定Endpoint.
 */
@Path("/vd0312")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0312Endpoint extends BaseEndpoint<Vd0312Request> {

	@Inject
	private Vd0312Service service;

	/**
	 * 初期化.
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0312Response init(Vd0312Request req) {
		return service.init(req);
	}

	/**
	 * ステップ追加.
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0312Response add(Vd0312Request req) {
		req.addStep = true;
		return service.add(req);
	}

	/**
	 * 承認者変更追加.
	 */
	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0312Response change(Vd0312Request req) {
		return service.change(req);
	}
}
