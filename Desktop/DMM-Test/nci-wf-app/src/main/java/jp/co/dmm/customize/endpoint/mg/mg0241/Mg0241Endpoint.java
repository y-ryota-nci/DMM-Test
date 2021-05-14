package jp.co.dmm.customize.endpoint.mg.mg0241;

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
 * 支払サイトマスタ編集画面Endpoint
 */
@Path("/mg0241")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0241Endpoint extends BaseEndpoint<Mg0241GetRequest> {

	@Inject private Mg0241Service service;

	/**
	 * 支払サイトマスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0241GetResponse init(Mg0241GetRequest req) {
		return service.init(req);
	}

	/**
	 * 支払サイトマスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0241UpdateResponse update(Mg0241UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 支払サイトマスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0241UpdateResponse insert(Mg0241UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 支払サイトマスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0241GetRequest req) {
		return service.insertCheck(req);
	}

}
