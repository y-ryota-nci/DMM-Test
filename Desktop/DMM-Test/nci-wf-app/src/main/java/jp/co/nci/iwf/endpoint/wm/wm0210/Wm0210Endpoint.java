package jp.co.nci.iwf.endpoint.wm.wm0210;

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
 * 参加者ロール登録Endpoint
 */
@Endpoint
@Path("/wm0210")
@RequiredLogin
@WriteAccessLog
public class Wm0210Endpoint extends BaseEndpoint<Wm0210Request> {
	@Inject
	private Wm0210Service service;

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
	public Wm0210Response init(Wm0210Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Path("/insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wm0210Response insert(Wm0210InsertRequest req) {
		return service.insert(req);
	}
}
