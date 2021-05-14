package jp.co.nci.iwf.endpoint.dc.dc0074;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * テンプレート明細登録Endpoint.
 */
@Endpoint
@Path("/dc0074")
@RequiredLogin
@WriteAccessLog
public class Dc0074Endpoint extends BaseEndpoint<Dc0074Request> {

	@Inject Dc0074Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0074Response init(Dc0074Request req) {
		return service.init(req);
	}

	/**
	 * 拡張項目(メタ項目)変更処理
	 * @param req
	 * @return
	 */
	@GET
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0074Response changeOption(@QueryParam("corporationCode") String corporationCode, @QueryParam("metaId") Long metaId) {
		return service.change(corporationCode, metaId);
	}

	/**
	 * 登録・更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0074Response save(Dc0074SaveRequest req) {
		return service.save(req);
	}
}
