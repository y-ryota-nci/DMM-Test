package jp.co.nci.iwf.endpoint.au.au0011;

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
import jp.co.nci.iwf.jersey.base.IEndpoint;

/**
 * パスワード変更画面(通常)Endpoint
 */
@Path("/au0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Au0011Endpoint extends BaseEndpoint<Au0011Request> implements IEndpoint<Au0011Request> {
	@Inject
	private Au0011Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Au0011Request req) {
		return service.init(req);
	}

	/**
	 * パスワードの保存
	 * @param req
	 * @return
	 */
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Au0011Request req) {
		return service.save(req);
	}
}
