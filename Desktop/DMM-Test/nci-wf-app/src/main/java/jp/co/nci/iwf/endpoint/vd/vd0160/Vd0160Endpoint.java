package jp.co.nci.iwf.endpoint.vd.vd0160;

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
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パーツツリー画面Endpoint
 */
@Path("/vd0160")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0160Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject
	private Vd0160Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0160InitResponse init(BaseRequest req) {
		return service.init(req);
	}

}
