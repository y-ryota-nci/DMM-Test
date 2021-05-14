package jp.co.nci.iwf.endpoint.vd.vd0091;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.vd.vd0090.Vd0091SaveRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 新規申請メニュー割当設定Endpoint
 */
@Path("/vd0091")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0091Endpoint extends BaseEndpoint<Vd0091InitRequest> {
	@Inject private Vd0091Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0091InitResponse init(Vd0091InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Vd0091SaveRequest req) {
		return service.save(req);
	}
}
