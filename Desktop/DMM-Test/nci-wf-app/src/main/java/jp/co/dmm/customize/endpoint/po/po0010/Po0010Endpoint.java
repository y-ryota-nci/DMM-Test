package jp.co.dmm.customize.endpoint.po.po0010;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 発注一覧Endpoint
 */
@Path("/po0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Po0010Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject private Po0010Service service;

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
	public BaseResponse search(Po0010SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 変更申請バリデーション
	 * @param req
	 * @return
	 */
	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Po0010ValidResponse validate(Po0010ValidRequest req) {
		return service.validate(req);
	}

	/**
	 * 完了
	 * @param req
	 * @return
	 */
	@POST
	@Path("/complete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse complete(Po0010SaveRequest req) {
		return service.complete(req);
	}
}
