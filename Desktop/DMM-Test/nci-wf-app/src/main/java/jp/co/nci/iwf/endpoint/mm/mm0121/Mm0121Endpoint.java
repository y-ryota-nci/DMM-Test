package jp.co.nci.iwf.endpoint.mm.mm0121;

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
 * お知らせ設定画面Endpoint
 */
@Path("/mm0121")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0121Endpoint extends BaseEndpoint<Mm0121InitRequest> {
	@Inject private Mm0121Service service;


	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0121Response init(Mm0121InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Mm0121Request req) {
		return service.save(req);
	}
}
