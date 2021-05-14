package jp.co.dmm.customize.endpoint.md.md0011;

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
 * 取引先登録画面Endpoint
 */
@Path("/md0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Md0011Endpoint extends BaseEndpoint<Md0011GetRequest> {

	@Inject private Md0011Service service;

	/**
	 * 取引先登録画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Md0011GetResponse init(Md0011GetRequest req) {
		return service.init(req);
	}

	/**
	 * 口座情報取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getAccountList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Md0011GetResponse getAccountList(Md0011GetRequest req) {
		return service.getAccountList(req);
	}
}
