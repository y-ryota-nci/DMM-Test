package jp.co.dmm.customize.endpoint.mg.mg0231;

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
 * 支払条件マスタ編集画面Endpoint
 */
@Path("/mg0231")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0231Endpoint extends BaseEndpoint<Mg0231GetRequest> {

	@Inject private Mg0231Service service;

	/**
	 * 支払条件マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0231GetResponse init(Mg0231GetRequest req) {
		return service.init(req);
	}

	/**
	 * 支払条件マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0231UpdateResponse update(Mg0231UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 支払条件マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0231UpdateResponse insert(Mg0231UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 支払条件マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0231GetRequest req) {
		return service.insertCheck(req);
	}

}
