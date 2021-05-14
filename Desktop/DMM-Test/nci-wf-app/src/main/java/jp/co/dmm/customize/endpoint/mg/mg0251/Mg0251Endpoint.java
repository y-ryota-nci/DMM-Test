package jp.co.dmm.customize.endpoint.mg.mg0251;

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
 * 部門関連マスタ編集画面Endpoint
 */
@Path("/mg0251")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0251Endpoint extends BaseEndpoint<Mg0251GetRequest> {

	@Inject private Mg0251Service service;

	/**
	 * 部門関連マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0251GetResponse init(Mg0251GetRequest req) {
		return service.init(req);
	}

	/**
	 * 部門関連マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0251UpdateResponse update(Mg0251UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 部門関連マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0251UpdateResponse insert(Mg0251UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 部門関連マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0251GetRequest req) {
		return service.insertCheck(req);
	}

}
