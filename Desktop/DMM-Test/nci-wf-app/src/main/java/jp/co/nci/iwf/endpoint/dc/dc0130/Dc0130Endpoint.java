package jp.co.nci.iwf.endpoint.dc.dc0130;

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
 * 業務文書メニューロール一覧画面Endpoint
 */
@Endpoint
@Path("/dc0130")
@RequiredLogin
@WriteAccessLog
public class Dc0130Endpoint extends BaseEndpoint<Dc0130Request> {

	@Inject
	private Dc0130Service service;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0130Response init(Dc0130Request req) {
		return service.init(req);
	}

	/**
	 * 検索
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0130Response search(Dc0130Request req) {
		return service.search(req);
	}

}
