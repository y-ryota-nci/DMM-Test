package jp.co.nci.iwf.endpoint.vd.vd0130;

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
 * コンテナ設定Endpoint
 */
@Path("/vd0130")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0130Endpoint extends BaseEndpoint<Vd0130InitRequest> {
	@Inject
	private Vd0130Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0130Response init(Vd0130InitRequest req) {
		return service.init(req);
	}
}
