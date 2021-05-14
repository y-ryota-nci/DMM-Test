package jp.co.nci.iwf.endpoint.up.up0100;

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
 * アップロード履歴画面Endpoint
 */
@Path("/up0100")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0100Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Up0100Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0100Response init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0100Response search(Up0100Request req) {
		return service.search(req);
	}

	/**
	 * アップロードファイル登録情報を取得
	 */
	@POST
	@Path("/getHistory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0100Response getHistory(Up0100Request req) {
		return service.getHistory(req);
	}

}
