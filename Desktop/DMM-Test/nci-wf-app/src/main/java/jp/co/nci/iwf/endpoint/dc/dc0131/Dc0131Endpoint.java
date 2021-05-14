package jp.co.nci.iwf.endpoint.dc.dc0131;

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
 * 業務文書公開設定画面Endpoint
 */
@Path("/dc0131")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0131Endpoint extends BaseEndpoint<Dc0131Request> {
	@Inject
	private Dc0131Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0131Response init(Dc0131Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0131Response create(Dc0131Request req) {
		return service.create(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0131Response update(Dc0131Request req) {
		return service.update(req);
	}

	/**
	 * 削除
	 */
	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0131Response remove(Dc0131Request req) {
		return service.remove(req);
	}

}
