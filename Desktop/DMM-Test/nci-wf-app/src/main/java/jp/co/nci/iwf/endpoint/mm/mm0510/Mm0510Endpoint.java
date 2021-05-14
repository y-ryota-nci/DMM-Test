package jp.co.nci.iwf.endpoint.mm.mm0510;

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
 * バージョン情報画面Endpoint
 */
@Path("/mm0510")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0510Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Mm0510Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0510InitResponse init(BaseRequest req) {
		return service.init(req);
	}
}
