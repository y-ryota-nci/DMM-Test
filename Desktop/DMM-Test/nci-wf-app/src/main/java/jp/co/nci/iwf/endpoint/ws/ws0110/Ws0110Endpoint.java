package jp.co.nci.iwf.endpoint.ws.ws0110;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.ws.WsSearchRequest;
import jp.co.nci.iwf.endpoint.ws.WsSearchResponse;
import jp.co.nci.iwf.endpoint.ws.WsSearchService;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 代理設定一覧(企業管理者用)Endpoint.
 */
@Endpoint
@Path("/ws0110")
@RequiredLogin
@WriteAccessLog
public class Ws0110Endpoint extends BaseEndpoint<WsSearchRequest> {

	@Inject
	private WsSearchService service;

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
	public WsSearchResponse init(WsSearchRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSearchResponse search(WsSearchRequest req) {
		return service.search(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSearchResponse delete(WsSearchRequest req) {
		return service.delete(req);
	}

	/**
	 * チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/check")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSearchResponse check(WsSearchRequest req) {
		return service.check(req);
	}
}
