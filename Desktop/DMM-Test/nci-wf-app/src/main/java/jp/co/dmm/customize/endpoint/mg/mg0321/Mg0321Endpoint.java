package jp.co.dmm.customize.endpoint.mg.mg0321;

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
 * 消費税関連マスタ編集画面Endpoint
 */
@Path("/mg0321")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0321Endpoint extends BaseEndpoint<Mg0321GetRequest> {

	@Inject private Mg0321Service service;

	/**
	 * 消費税関連マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0321GetResponse init(Mg0321GetRequest req) {
		return service.init(req);
	}

	/**
	 * 消費税関連マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0321UpdateResponse update(Mg0321UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 消費税関連マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0321UpdateResponse insert(Mg0321UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 消費税関連マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0321GetRequest req) {
		return service.insertCheck(req);
	}

}
