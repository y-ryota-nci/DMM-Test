package jp.co.nci.iwf.endpoint.dc.dc0072;

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
 * テンプレート設定Endpoint.
 */
@Endpoint
@Path("/dc0072")
@RequiredLogin
@WriteAccessLog
public class Dc0072Endpoint extends BaseEndpoint<Dc0072Request> {

	@Inject Dc0072Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0072Response init(Dc0072Request req) {
		return service.init(req);
	}

	/**
	 * テンプレート明細検索.
	 * @param req
	 * @return
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0072Response search(Dc0072Request req) {
		return service.search(req);
	}

	/**
	 * 登録・更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0072Response save(Dc0072SaveRequest req) {
		return service.save(req);
	}

	/**
	 * テンプレート明細削除
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0072Response delete(Dc0072DeleteRequest req) {
		return service.save(req);
	}
}
