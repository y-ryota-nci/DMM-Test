package jp.co.dmm.customize.endpoint.mg.mg0191;

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
 * 部門マスタ設定画面Endpoint
 */
@Path("/mg0191")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0191Endpoint extends BaseEndpoint<Mg0191GetRequest> {

	@Inject private Mg0191Service service;

	/**
	 * 部門マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0191GetResponse init(Mg0191GetRequest req) {
		return service.init(req);
	}

	/**
	 * 部門マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0191UpdateResponse update(Mg0191UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 部門マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0191UpdateResponse insert(Mg0191UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 部門チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0191GetRequest req) {
		return service.insertCheck(req);
	}

}
