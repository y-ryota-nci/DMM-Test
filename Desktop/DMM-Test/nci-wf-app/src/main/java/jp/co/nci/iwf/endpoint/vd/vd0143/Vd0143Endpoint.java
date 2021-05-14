package jp.co.nci.iwf.endpoint.vd.vd0143;

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
 * パーツ計算式定義一覧Endpoint.
 */
@Endpoint
@Path("/vd0143")
@RequiredLogin
@WriteAccessLog
public class Vd0143Endpoint extends BaseEndpoint<Vd0143Request> {

	@Inject
	private Vd0143Service service;

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
	public Vd0143Response init(Vd0143Request req) {
		return service.init(req);
	}

}
