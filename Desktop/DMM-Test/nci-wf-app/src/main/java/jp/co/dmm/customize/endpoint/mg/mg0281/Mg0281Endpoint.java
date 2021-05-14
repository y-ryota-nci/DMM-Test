package jp.co.dmm.customize.endpoint.mg.mg0281;

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
 * メディアマスタ編集画面Endpoint
 */
@Path("/mg0281")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0281Endpoint extends BaseEndpoint<Mg0281GetRequest> {

	@Inject private Mg0281Service service;

	/**
	 * メディアマスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0281GetResponse init(Mg0281GetRequest req) {
		return service.init(req);
	}

	/**
	 * メディアマスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0281UpdateResponse update(Mg0281UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * メディアマスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0281UpdateResponse insert(Mg0281UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * メディアマスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0281GetRequest req) {
		return service.insertCheck(req);
	}

}
