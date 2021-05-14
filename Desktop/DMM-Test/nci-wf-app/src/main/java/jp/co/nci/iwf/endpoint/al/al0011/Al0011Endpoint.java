package jp.co.nci.iwf.endpoint.al.al0011;

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
 * アクセスログ詳細Endpoint
 */
@Path("/al0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Al0011Endpoint extends BaseEndpoint<Al0011InitRequest> {
	@Inject
	private Al0011Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Al0011InitResponse init(Al0011InitRequest req) {
		return service.init(req);
	}
}
