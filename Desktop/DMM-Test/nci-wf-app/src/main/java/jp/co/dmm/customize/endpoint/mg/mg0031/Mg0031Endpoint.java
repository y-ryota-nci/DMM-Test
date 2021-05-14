package jp.co.dmm.customize.endpoint.mg.mg0031;

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
 * 費目関連マスタ設定画面Endpoint
 */
@Path("/mg0031")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0031Endpoint extends BaseEndpoint<Mg0031GetRequest> {

	@Inject private Mg0031Service service;

	/**
	 * 費目関連マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0031GetResponse init(Mg0031GetRequest req) {
		return service.init(req);
	}

	/**
	 * 費目関連マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0031UpdateResponse update(Mg0031UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 費目関連マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0031UpdateResponse insert(Mg0031UpdateRequest req) {
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
	public boolean researchCheck(Mg0031GetRequest req) {
		return service.insertCheck(req);
	}

}
