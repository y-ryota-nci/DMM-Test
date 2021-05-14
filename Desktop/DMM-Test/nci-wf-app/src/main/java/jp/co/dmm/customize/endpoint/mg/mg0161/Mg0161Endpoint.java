package jp.co.dmm.customize.endpoint.mg.mg0161;

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
 * 消費税マスタ編集画面Endpoint
 */
@Path("/mg0161")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0161Endpoint extends BaseEndpoint<Mg0161GetRequest> {

	@Inject private Mg0161Service service;

	/**
	 * 消費税マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0161GetResponse init(Mg0161GetRequest req) {
		return service.init(req);
	}

	/**
	 * 消費税マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0161UpdateResponse update(Mg0161UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 消費税マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0161UpdateResponse insert(Mg0161UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 消費税マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0161UpdateRequest req) {
		return service.insertCheck(req);
	}

	/**
	 * 消費税マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0161UpdateRequest req) {
		return service.updateCheck(req);
	}

}
