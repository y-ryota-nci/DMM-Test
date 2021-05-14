package jp.co.nci.iwf.endpoint.wl.wl0020;

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
 * 個人用トレイ選択画面Endpoint
 */
@Path("/wl0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0020Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Wl0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Wl0020InitResponse init(BaseRequest req) {
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
	public BaseResponse save(Wl0020SaveRequest req) {
		return service.save(req);
	}
}
