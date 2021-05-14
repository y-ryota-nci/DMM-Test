package jp.co.nci.iwf.endpoint.vd.vd0320;

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
 * 独立画面パーツ用ポップアップ画面Endpoint
 */
@Path("/vd0320")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0320Endpoint extends BaseEndpoint<Vd0320Request> {
	@Inject private Vd0320Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0320Response init(Vd0320Request req) {
		return service.init(req);
	}
}
