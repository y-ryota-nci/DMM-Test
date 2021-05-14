package jp.co.nci.iwf.endpoint.ti.ti0020;

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

/**
 * メニューロール一覧（マスタ権限設定）
 */
@Path("/ti0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0020Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Ti0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0020Response init(BaseRequest req) {
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
	public Ti0020Response search(Ti0020Request req) {
		return service.search(req);
	}
}
