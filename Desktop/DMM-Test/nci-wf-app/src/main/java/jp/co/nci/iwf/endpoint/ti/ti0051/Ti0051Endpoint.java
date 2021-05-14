package jp.co.nci.iwf.endpoint.ti.ti0051;

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
 * 汎用テーブル検索条件設定Endpoint
 */
@Path("/ti0051")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0051Endpoint extends BaseEndpoint<Ti0051InitRequest> {
	@Inject private Ti0051Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0051Response init(Ti0051InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ti0051Response save(Ti0051SaveRequest req) {
		return service.save(req);
	}
}
