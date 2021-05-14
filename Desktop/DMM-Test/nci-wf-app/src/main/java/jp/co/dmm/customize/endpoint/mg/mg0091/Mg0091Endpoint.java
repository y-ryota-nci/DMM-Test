package jp.co.dmm.customize.endpoint.mg.mg0091;

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
 * 銀行口座マスタ設定画面Endpoint
 */
@Path("/mg0091")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0091Endpoint extends BaseEndpoint<Mg0091GetRequest> {

	@Inject private Mg0091Service service;

	/**
	 * 銀行口座マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0091GetResponse init(Mg0091GetRequest req) {
		return service.init(req);
	}

	/**
	 * 銀行口座マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0091UpdateResponse update(Mg0091UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 品目チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0091UpdateRequest req) {
		return service.insertCheck(req);
	}

	/**
	 * 品目チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0091UpdateRequest req) {
		return service.updateCheck(req);
	}

	/**
	 * 銀行口座マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0091UpdateResponse insert(Mg0091UpdateRequest req) {
		return service.insert(req);
	}

}
