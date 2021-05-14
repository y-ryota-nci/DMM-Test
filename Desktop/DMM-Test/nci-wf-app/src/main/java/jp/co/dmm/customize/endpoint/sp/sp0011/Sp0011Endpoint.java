package jp.co.dmm.customize.endpoint.sp.sp0011;

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
@Path("/sp0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Sp0011Endpoint extends BaseEndpoint<Sp0011GetRequest> {

	@Inject private Sp0011Service service;

	/**
	 * 取引先登録画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Sp0011GetResponse init(Sp0011GetRequest req) {
		return service.init(req);
	}

	/**
	 * 取引先情報取得
	 */
	@POST
	@Path("/getSplr")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0011GetResponse getSplr(Sp0011GetRequest req) {
		return service.get(req);
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
	public Sp0011GetResponse getAccountList(Sp0011GetRequest req) {
		return service.getAccountList(req);
	}

	/**
	 * 取引先登録画面更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0011UpdateResponse update(Sp0011UpdateRequest req) {
		return service.update(req);
	}

}
