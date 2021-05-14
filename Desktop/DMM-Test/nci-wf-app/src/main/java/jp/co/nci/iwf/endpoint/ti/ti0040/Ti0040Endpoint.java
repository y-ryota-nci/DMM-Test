package jp.co.nci.iwf.endpoint.ti.ti0040;

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
 * 汎用テーブル定義画面Endpoint
 */
@Path("/ti0040")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0040Endpoint extends BaseEndpoint<Ti0040InitRequest> {
	@Inject private Ti0040Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0040Response init(Ti0040InitRequest req) {
		return service.init(req);
	}

}
