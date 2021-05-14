package jp.co.dmm.customize.endpoint.mg.mg0181;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 社内レートマスタ設定画面Endpoint
 */
@Path("/mg0181")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0181Endpoint extends BaseEndpoint<Mg0181Request> {

	@Inject private Mg0181Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Mg0181Request req) {
		return service.init(req);
	}

	/**
	 * 社内レート存在チェック（登録用）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0181Request req) {
		return service.insertCheck(req);
	}

	/**
	 * 社内レート存在チェック（更新用）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0181Request req) {
		return service.updateCheck(req);
	}

	/**
	 * 社内レート登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0181Response insert(Mg0181Request req) {
		return service.insert(req);
	}

	/**
	 * 社内レートマスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0181Response update(Mg0181Request req) {
		return service.update(req);
	}
}
