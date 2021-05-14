package jp.co.dmm.customize.endpoint.mg.mg0261;

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
 * ｸﾚｶ口座マスタ編集画面Endpoint
 */
@Path("/mg0261")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0261Endpoint extends BaseEndpoint<Mg0261GetRequest> {

	@Inject private Mg0261Service service;

	/**
	 * ｸﾚｶ口座マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0261GetResponse init(Mg0261GetRequest req) {
		return service.init(req);
	}

	/**
	 * ｸﾚｶ口座マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0261UpdateResponse update(Mg0261UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * ｸﾚｶ口座マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0261UpdateResponse insert(Mg0261UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * ｸﾚｶ口座マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0261GetRequest req) {
		return service.insertCheck(req);
	}

}
