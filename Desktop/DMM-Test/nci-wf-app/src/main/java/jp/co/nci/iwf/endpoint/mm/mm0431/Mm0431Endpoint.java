package jp.co.nci.iwf.endpoint.mm.mm0431;

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
 * 役職設定Endpoint
 */
@Path("/mm0431")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0431Endpoint extends BaseEndpoint<Mm0431Request> {
	@Inject
	private Mm0431Service service;

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
	public Mm0431Response init(Mm0431Request req) {
		return service.init(req);
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
	public Mm0431Response update(Mm0431UpdateRequest req) {
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
	public Mm0431Response delete(Mm0431Request req) {
		return service.delete(req);
	}
}
