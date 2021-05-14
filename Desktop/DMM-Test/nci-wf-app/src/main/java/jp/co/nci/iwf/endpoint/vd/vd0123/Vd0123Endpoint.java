package jp.co.nci.iwf.endpoint.vd.vd0123;

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
 * 外部Javascript参照設定
 */
@Path("/vd0123")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0123Endpoint extends BaseEndpoint<Vd0123InitRequest> {
	@Inject
	private Vd0123Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0123InitResponse init(Vd0123InitRequest req) {
		return service.init(req);
	}

}
