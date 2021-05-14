package jp.co.nci.iwf.endpoint.mm.mm0411;

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
 * 企業設定Endpoint
 */
@Endpoint
@Path("/mm0411")
@RequiredLogin
@WriteAccessLog
public class Mm0411Endpoint extends BaseEndpoint<Mm0411Request> {
	@Inject
	private Mm0411Service service;

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
	public Mm0411Response init(Mm0411Request req) {
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
	public Mm0411Response insert(Mm0411InsertRequest req) {
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
	public Mm0411Response update(Mm0411InsertRequest req) {
		return service.update(req);
	}
}
