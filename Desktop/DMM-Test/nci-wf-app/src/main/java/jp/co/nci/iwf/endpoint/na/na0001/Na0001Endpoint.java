package jp.co.nci.iwf.endpoint.na.na0001;

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
 * メニューロール一覧Endpoint
 */
@Endpoint
@Path("/na0001")
@RequiredLogin
@WriteAccessLog
public class Na0001Endpoint extends BaseEndpoint<Na0001Request> {

	@Inject
	private Na0001Service service;

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
	public Na0001Response init(Na0001Request req) {
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
	public Na0001Response search(Na0001Request req) {
		return service.search(req);
	}
}
