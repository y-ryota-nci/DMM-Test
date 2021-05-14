package jp.co.dmm.customize.endpoint.mg.mg0101;

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
 * 銀行マスタ編集画面Endpoint
 */
@Path("/mg0101")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0101Endpoint extends BaseEndpoint<Mg0101GetRequest> {

	@Inject private Mg0101Service service;

	/**
	 * 銀行マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0101GetResponse init(Mg0101GetRequest req) {
		return service.init(req);
	}

	/**
	 * 銀行マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0101UpdateResponse update(Mg0101UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 銀行マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0101UpdateResponse insert(Mg0101UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 銀行チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0101GetRequest req) {
		return service.insertCheck(req);
	}

}
