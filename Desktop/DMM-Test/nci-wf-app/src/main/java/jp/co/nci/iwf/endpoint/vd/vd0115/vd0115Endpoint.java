package jp.co.nci.iwf.endpoint.vd.vd0115;

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
 * プレビュー画面Endpoint
 */
@Path("/vd0115")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class vd0115Endpoint extends BaseEndpoint<Vd0115Request> {
	@Inject
	private Vd0115Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0115Response init(Vd0115Request req) {
		return service.init(req);
	}

	/**
	 * 業務管理項目Mapを生成
	 */
	@POST
	@Path("/createBusinessInfoMap")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0115Response createBusinessInfoMap(Vd0115Request req) {
		return service.createBusinessInfoMap(req);
	}

	/**
	 * サーバー側のバリデーション
	 */
	@POST
	@Path("/validateServer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0115Response validateServer(Vd0115Request req) {
		return service.validateServer(req);
	}
}
