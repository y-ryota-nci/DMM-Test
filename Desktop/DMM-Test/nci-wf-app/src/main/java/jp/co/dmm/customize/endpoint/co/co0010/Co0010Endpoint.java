package jp.co.dmm.customize.endpoint.co.co0010;

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
 * 契約一覧Endpoint
 */
@Path("/co0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Co0010Endpoint extends BaseEndpoint<Co0010SearchRequest> {

	@Inject private Co0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Co0010SearchResponse init(Co0010SearchRequest req) {
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
	public Co0010SearchResponse search(Co0010SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Co0010GetScreenProcessIdResponse getScreenProcessId(Co0010GetScreenProcessIdRequest req) {
		return service.validate(req);
	}

}
