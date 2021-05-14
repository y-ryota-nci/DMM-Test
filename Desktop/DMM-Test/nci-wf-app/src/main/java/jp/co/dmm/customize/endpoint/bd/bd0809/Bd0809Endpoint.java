package jp.co.dmm.customize.endpoint.bd.bd0809;

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
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算入力画面Endpoint
 */
@Path("/bd0809")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0809Endpoint extends BaseEndpoint<Bd0809InitRequest> {
	@Inject private Bd0809Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Bd0809InitRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse search(Bd0809Request req) {
		return service.search(req);
	}
}
