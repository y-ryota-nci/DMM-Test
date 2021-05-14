package jp.co.dmm.customize.endpoint.mg.mg0171;

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
 * 通貨マスタ編集画面Endpoint
 */
@Path("/mg0171")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0171Endpoint extends BaseEndpoint<Mg0171GetRequest> {

	@Inject private Mg0171Service service;

	/**
	 * 通貨マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0171GetResponse init(Mg0171GetRequest req) {
		return service.init(req);
	}

	/**
	 * 通貨マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0171UpdateResponse update(Mg0171UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 通貨マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0171UpdateResponse insert(Mg0171UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 通貨マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0171GetRequest req) {
		return service.insertCheck(req);
	}

}
