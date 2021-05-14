package jp.co.nci.iwf.endpoint.mm.mm0030;

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
 * 業務管理項目名称設定Endpoint
 */
@Path("/mm0030")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0030Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject
	private Mm0030Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0030Response init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Mm0030SaveRequest req) {
		return service.save(req);
	}

	/**
	 * リセット
	 */
	@POST
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0030Response reset(BaseRequest req) {
		return service.reset(req);
	}

}
