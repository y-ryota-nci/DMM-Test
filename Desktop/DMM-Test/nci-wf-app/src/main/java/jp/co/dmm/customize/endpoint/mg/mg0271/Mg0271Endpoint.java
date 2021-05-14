package jp.co.dmm.customize.endpoint.mg.mg0271;

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
 * 予算科目マスタ編集画面Endpoint
 */
@Path("/mg0271")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0271Endpoint extends BaseEndpoint<Mg0271GetRequest> {

	@Inject private Mg0271Service service;

	/**
	 * 予算科目マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0271GetResponse init(Mg0271GetRequest req) {
		return service.init(req);
	}

	/**
	 * 予算科目マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0271UpdateResponse update(Mg0271UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 予算科目マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0271UpdateResponse insert(Mg0271UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 予算科目マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0271GetRequest req) {
		return service.insertCheck(req);
	}

}
