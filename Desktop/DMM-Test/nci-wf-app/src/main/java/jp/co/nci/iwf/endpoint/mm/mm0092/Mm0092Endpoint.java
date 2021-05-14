package jp.co.nci.iwf.endpoint.mm.mm0092;

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
 * ルックアップグループ設定Endpoint
 */
@Endpoint
@Path("/mm0092")
@RequiredLogin
@WriteAccessLog
public class Mm0092Endpoint extends BaseEndpoint<Mm0092Request> {
	@Inject
	private Mm0092Service service;

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
	public Mm0092Response init(Mm0092Request req) {
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
	public Mm0092Response search(Mm0092Request req) {
		return service.search(req);
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
	public Mm0092Response update(Mm0092UpdateRequest req) {
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
	public Mm0092Response delete(Mm0092Request req) {
		return service.delete(req);
	}
}
