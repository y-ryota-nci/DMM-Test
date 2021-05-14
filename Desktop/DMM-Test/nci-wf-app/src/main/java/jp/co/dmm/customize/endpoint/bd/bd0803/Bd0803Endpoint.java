package jp.co.dmm.customize.endpoint.bd.bd0803;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算履歴メンテナンスEndpoint
 */
@Path("/bd0803")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0803Endpoint extends BaseEndpoint<Bd0803Request> {
	@Inject private Bd0803Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Bd0803Request req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse search(Bd0803Request req) {
		return service.search(req);
	}

	/**
	 * 削除
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Bd0803Request req) {
		return service.delete(req);
	}

}
