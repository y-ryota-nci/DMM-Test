package jp.co.nci.iwf.endpoint.vd.vd0034;

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
 * 画面属性コピー設定画面Endpoint
 */
@Path("/vd0034")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0034Endpoint extends BaseEndpoint<Vd0034Request> {
	@Inject private Vd0034Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Vd0034Request req) {
		return service.init(req);
	}

	/**
	 * コピー元画面を変更
	 */
	@POST
	@Path("/changeScreen")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse changeScreen(Vd0034Request req) {
		return service.changeScreen(req);
	}

	/**
	 * コピー元画面を変更
	 */
	@POST
	@Path("/copy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse copy(Vd0034Request req) {
		return service.copy(req);
	}
}
