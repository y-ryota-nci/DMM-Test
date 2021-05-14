package jp.co.nci.iwf.endpoint.wm.wm0200;

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
 * 参加者ロール一覧Endpoint
 */
@Endpoint
@Path("/wm0200")
@RequiredLogin
@WriteAccessLog
public class Wm0200Endpoint extends BaseEndpoint<Wm0200Request> {

	@Inject
	private Wm0200Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Wm0200Response init(Wm0200Request req) {
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
	public Wm0200Response search(Wm0200Request req) {
		return service.search(req);
	}

	/**
	 * 追加
	 * @param req
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wm0200Response add(Wm0200Request req) {
		return service.add(req);
	}
}
