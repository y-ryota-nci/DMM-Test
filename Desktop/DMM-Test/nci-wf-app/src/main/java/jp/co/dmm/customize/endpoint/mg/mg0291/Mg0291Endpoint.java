package jp.co.dmm.customize.endpoint.mg.mg0291;

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
 * 結合フロアマスタ編集画面Endpoint
 */
@Path("/mg0291")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0291Endpoint extends BaseEndpoint<Mg0291GetRequest> {

	@Inject private Mg0291Service service;

	/**
	 * 結合フロアマスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0291GetResponse init(Mg0291GetRequest req) {
		return service.init(req);
	}

	/**
	 * 結合フロアマスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0291UpdateResponse update(Mg0291UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 結合フロアマスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0291UpdateResponse insert(Mg0291UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 結合フロアマスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0291GetRequest req) {
		return service.insertCheck(req);
	}

}
