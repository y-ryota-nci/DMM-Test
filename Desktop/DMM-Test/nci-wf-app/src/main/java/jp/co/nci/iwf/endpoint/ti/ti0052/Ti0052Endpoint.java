package jp.co.nci.iwf.endpoint.ti.ti0052;

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
 * 汎用テーブル検索条件カラム設定Endpoint
 */
@Path("/ti0052")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0052Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Ti0052Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0052Response init(BaseRequest req) {
		return service.init(req);
	}
}
