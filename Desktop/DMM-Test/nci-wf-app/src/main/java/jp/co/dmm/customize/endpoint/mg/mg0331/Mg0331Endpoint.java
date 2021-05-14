package jp.co.dmm.customize.endpoint.mg.mg0331;

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
 * 国マスタ編集画面Endpoint
 */
@Path("/mg0331")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0331Endpoint extends BaseEndpoint<Mg0331Request> {

	@Inject private Mg0331Service service;

	/**
	 * 国マスタ編集画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0331Response init(Mg0331Request req) {
		return service.init(req);
	}

	/**
	 * 国マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0331SaveResponse update(Mg0331SaveRequest req) {
		return service.update(req);
	}

	/**
	 * 国マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0331SaveResponse insert(Mg0331SaveRequest req) {
		return service.insert(req);
	}

	/**
	 * 国マスタチェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean researchCheck(Mg0331Request req) {
		return service.insertCheck(req);
	}

}
