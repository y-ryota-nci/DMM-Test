package jp.co.nci.iwf.endpoint.mm.mm0051;

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
 * 採番形式登録Endpoint
 */
@Endpoint
@Path("/mm0051")
@RequiredLogin
@WriteAccessLog
public class Mm0051Endpoint extends BaseEndpoint<Mm0051InitRequest> {
	@Inject
	private Mm0051Service service;

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
	public Mm0051Response init(Mm0051InitRequest req) {
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
	public Mm0051Response insert(Mm0051Request req) {
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
	public Mm0051Response update(Mm0051Request req) {
		return service.update(req);
	}

	/**
	 * 連番
	 * @return
	 */
	@Path("/getSequenceList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0051Response getSequenceList(Mm0051InitRequest req) {
		return service.getSequenceList(req);
	}
}
