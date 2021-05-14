package jp.co.dmm.customize.endpoint.mg.mg0211;

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
 * 源泉税区分マスタ設定画面Endpoint
 */
@Path("/mg0211")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0211Endpoint extends BaseEndpoint<Mg0211Request> {

	@Inject private Mg0211Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0211Response init(Mg0211Request req) {
		return service.init(req);
	}

	/**
	 * 源泉税区分存在チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0211Request req) {
		return service.insertCheck(req);
	}

	/**
	 * 源泉税区分登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0211Response insert(Mg0211Request req) {
		return service.insert(req);
	}

	/**
	 * 源泉税区分マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0211Response update(Mg0211Request req) {
		return service.update(req);
	}
}
