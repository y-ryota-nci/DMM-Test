package jp.co.nci.iwf.endpoint.mm.mm0130;

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

/**
 * メニュー編集Endpoint
 */
@Path("/mm0130")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0130Endpoint extends BaseEndpoint<Mm0130InitRequest>{

	@Inject
	private Mm0130Service service;

	/**
	 * 初期化処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0130InitResponse init(Mm0130InitRequest req) {
		return service.init(req);
	}

	/**
	 * 更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0130SaveResponse update(Mm0130SaveRequest req) {
		return service.update(req);
	}

}
