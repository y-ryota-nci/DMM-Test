package jp.co.nci.iwf.endpoint.mm.mm0500;

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
 * マスター初期値設定Endpoint
 */
@Path("/mm0500")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0500Endpoint extends BaseEndpoint<Mm0500Request> {
	@Inject private Mm0500Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0500Response init(Mm0500Request req) {
		return service.init(req);
	}

	/**
	 * 設定
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0500Response save(Mm0500Request req) {
		return service.save(req);
	}

	/**
	 * コピー元企業の変更時
	 */
	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0500Response change(Mm0500Request req) {
		return service.init(req);
	}
}
