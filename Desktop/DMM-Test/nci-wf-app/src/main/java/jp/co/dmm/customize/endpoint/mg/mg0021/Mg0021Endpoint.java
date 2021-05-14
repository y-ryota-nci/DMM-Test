package jp.co.dmm.customize.endpoint.mg.mg0021;

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
 * 費目マスタ設定画面Endpoint
 */
@Path("/mg0021")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0021Endpoint extends BaseEndpoint<Mg0021GetRequest> {

	@Inject private Mg0021Service service;

	/**
	 * 費目マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0021GetResponse init(Mg0021GetRequest req) {
		return service.init(req);
	}

	/**
	 * 費目マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0021UpdateResponse update(Mg0021UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 費目マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0021UpdateResponse insert(Mg0021UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 費目チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0021GetRequest req) {
		return service.insertCheck(req);
	}

}
