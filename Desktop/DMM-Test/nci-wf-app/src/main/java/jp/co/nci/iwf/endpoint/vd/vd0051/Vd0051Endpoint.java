package jp.co.nci.iwf.endpoint.vd.vd0051;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * Javascript設定Endpoint
 */
@Path("/vd0051")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0051Endpoint extends BaseEndpoint<Vd0051InitRequest> {
	@Inject
	private Vd0051Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0051InitResponse init(Vd0051InitRequest req) {
		return service.init(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Vd0051SaveRequest req) {
		return service.save(req);
	}
}
