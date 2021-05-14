package jp.co.dmm.customize.endpoint.po.po0020;

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
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 管理_定期発注マスタ一覧 Endpoint
 */
@Path("/po0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Po0020Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject private Po0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
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
	public BasePagingResponse search(Po0020SearchRequest req) {
		return service.search(req);
	}
}
