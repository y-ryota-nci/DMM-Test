package jp.co.nci.iwf.endpoint.dc.dc0230;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.dc.dc0200.Dc0200InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0200.Dc0200SaveRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書トレイ選択(管理者用)Endpoint
 */
@Path("/dc0230")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0230Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Dc0230Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0200InitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 更新処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Dc0200SaveRequest req) {
		return service.save(req);
	}
}
