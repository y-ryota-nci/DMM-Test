package jp.co.nci.iwf.endpoint.mm.mm0040;

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
 * 文書管理項目名称設定Endpoint
 */
@Path("/mm0040")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0040Endpoint extends BaseEndpoint<Mm0040Request> {
	@Inject
	private Mm0040Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0040Response init(Mm0040Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Mm0040SaveRequest req) {
		return service.save(req);
	}

//	/**
//	 * リセット
//	 * リセット機能は不要と思われる。必要ならコメントアウトを外してください
//	 */
//	@POST
//	@Path("/reset")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Mm0040Response reset(BaseRequest req) {
//		return service.reset(req);
//	}

}
