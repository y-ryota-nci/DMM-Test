package jp.co.nci.iwf.endpoint.mm.mm0081;

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
 * アクション機能登録Endpoint
 */
@Endpoint
@Path("/mm0081")
@RequiredLogin
@WriteAccessLog
public class Mm0081Endpoint extends BaseEndpoint<Mm0081Request> {
	@Inject
	private Mm0081Service service;

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
	public Mm0081Response init(Mm0081Request req) {
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
	public Mm0081Response insert(Mm0081InsertRequest req) {
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
	public Mm0081Response update(Mm0081InsertRequest req) {
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
	public Mm0081Response delete(Mm0081InsertRequest req) {
		return service.delete(req);
	}
}
