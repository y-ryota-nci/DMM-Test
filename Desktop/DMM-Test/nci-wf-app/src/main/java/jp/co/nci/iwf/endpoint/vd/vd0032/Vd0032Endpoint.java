package jp.co.nci.iwf.endpoint.vd.vd0032;

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
 * 画面パーツツリー画面Endpoint.
 */
@Path("/vd0032")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0032Endpoint extends BaseEndpoint<Vd0032InitRequest> {
	@Inject
	private Vd0032Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0032InitResponse init(Vd0032InitRequest req) {
		return service.init(req);
	}

}
