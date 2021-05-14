package jp.co.nci.iwf.endpoint.rm.rm0111;

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
 * メニュー業務管理項目名称設定Endpoint
 */
@Path("/rm0111")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Rm0111Endpoint extends BaseEndpoint<Rm0111Request> {
	@Inject
	private Rm0111Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Rm0111Response init(Rm0111Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Rm0111SaveRequest req) {
		return service.save(req);
	}
}
