package jp.co.nci.iwf.endpoint.mm.mm0401;

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
 * 企業グループマスタ設定Endpoint
 */
@Endpoint
@Path("/mm0401")
@RequiredLogin
@WriteAccessLog
public class Mm0401Endpoint extends BaseEndpoint<Mm0401InitRequest> {
	@Inject private Mm0401Service service;

	/**
	 * 初期化
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0401InitResponse init(Mm0401InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0401InitResponse save(Mm0401Request req) {
		return service.save(req);
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
	public Mm0401InitResponse delete(Mm0401Request req) {
		return service.delete(req);
	}
}
