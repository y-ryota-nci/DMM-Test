package jp.co.dmm.customize.endpoint.ct.ct0010;

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
 * カタログ検索画面Endpoint
 */
@Path("/ct0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ct0010Endpoint extends BaseEndpoint<Ct0010InitRequest> {

	@Inject private Ct0010Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0010InitResponse init(Ct0010InitRequest req) {
		return service.init(req);
	}


	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0010SearchResponse getWorklist(Ct0010SearchRequest req) {
		return service.search(req);
	}
}
