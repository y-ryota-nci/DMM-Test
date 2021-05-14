package jp.co.dmm.customize.endpoint.mg.mg0141;

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
 * 勘定科目補助マスタ設定画面Endpoint
 */
@Path("/mg0141")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0141Endpoint extends BaseEndpoint<Mg0141GetRequest> {

	@Inject private Mg0141Service service;

	/**
	 * 勘定科目補助マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0141GetResponse init(Mg0141GetRequest req) {
		return service.init(req);
	}

	/**
	 * 勘定科目補助マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0141UpdateResponse update(Mg0141UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 勘定科目補助マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0141UpdateResponse insert(Mg0141UpdateRequest req) {
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
	public boolean insertCheck(Mg0141UpdateRequest req) {
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
	public boolean updateCheck(Mg0141UpdateRequest req) {
		return service.updateCheck(req);
	}

}
