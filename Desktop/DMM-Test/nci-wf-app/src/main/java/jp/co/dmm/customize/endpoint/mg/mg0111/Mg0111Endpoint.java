package jp.co.dmm.customize.endpoint.mg.mg0111;

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
 * 銀行支店マスタ設定画面Endpoint
 */
@Path("/mg0111")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0111Endpoint extends BaseEndpoint<Mg0111GetRequest> {

	@Inject private Mg0111Service service;

	/**
	 * 銀行マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0111GetResponse init(Mg0111GetRequest req) {
		return service.init(req);
	}

	/**
	 * 銀行支店マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0111UpdateResponse update(Mg0111UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 銀行支店登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0111UpdateResponse insert(Mg0111UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 銀行支店チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0111GetRequest req) {
		return service.insertCheck(req);
	}

}
