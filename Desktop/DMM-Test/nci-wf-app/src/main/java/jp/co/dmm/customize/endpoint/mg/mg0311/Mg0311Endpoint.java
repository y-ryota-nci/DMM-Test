package jp.co.dmm.customize.endpoint.mg.mg0311;

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
 * 住所マスタ編集画面Endpoint
 */
@Path("/mg0311")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0311Endpoint extends BaseEndpoint<Mg0311GetRequest> {

	@Inject private Mg0311Service service;

	/**
	 * 住所マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0311GetResponse init(Mg0311GetRequest req) {
		return service.init(req);
	}

	/**
	 * 郵便番号マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0311UpdateResponse update(Mg0311UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 郵便番号マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0311UpdateResponse insert(Mg0311UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 郵便番号マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0311UpdateRequest req) {
		return service.insertCheck(req);
	}

	/**
	 * 郵便番号マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0311UpdateRequest req) {
		return service.updateCheck(req);
	}

}
