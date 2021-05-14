package jp.co.dmm.customize.endpoint.mg.mg0131;

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
 * 勘定科目マスタ設定画面Endpoint
 */
@Path("/mg0131")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0131Endpoint extends BaseEndpoint<Mg0131GetRequest> {

	@Inject private Mg0131Service service;

	/**
	 * 勘定科目マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0131GetResponse init(Mg0131GetRequest req) {
		return service.init(req);
	}

	/**
	 * 勘定科目マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0131UpdateResponse update(Mg0131UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 勘定科目マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0131UpdateResponse insert(Mg0131UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 勘定科目存在チェック（登録用）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0131UpdateRequest req) {
		return service.insertCheck(req);
	}

	/**
	 * 勘定科目存在チェック（更新用）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0131UpdateRequest req) {
		return service.updateCheck(req);
	}

}
