package jp.co.nci.iwf.endpoint.wm.wm0330;

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
 * 参加者変更ロール構成登録Endpoint
 */
@Endpoint
@Path("/wm0330")
@RequiredLogin
@WriteAccessLog
public class Wm0330Endpoint extends BaseEndpoint<Wm0330Request> {
	@Inject
	private Wm0330Service service;

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
	public Wm0330Response init(Wm0330Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Path("/insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wm0330Response insert(Wm0330InsertRequest req) {
		return service.insert(req);
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wm0330Response update(Wm0330InsertRequest req) {
		return service.update(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wm0330Response delete(Wm0330InsertRequest req) {
		return service.delete(req);
	}
}
