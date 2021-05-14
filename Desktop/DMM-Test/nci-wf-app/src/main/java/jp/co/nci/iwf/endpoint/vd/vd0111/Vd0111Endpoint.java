package jp.co.nci.iwf.endpoint.vd.vd0111;

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
 * パーツ条件定義一覧Endpoint.
 */
@Endpoint
@Path("/vd0111")
@RequiredLogin
@WriteAccessLog
public class Vd0111Endpoint extends BaseEndpoint<Vd0111Request> {

	@Inject
	private Vd0111Service service;

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
	public Vd0111Response init(Vd0111Request req) {
		return service.init(req);
	}

}
