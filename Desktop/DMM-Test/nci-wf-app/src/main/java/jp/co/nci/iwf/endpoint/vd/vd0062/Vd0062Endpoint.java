package jp.co.nci.iwf.endpoint.vd.vd0062;

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
 * 選択肢マスタ設定Endpoint.
 */
@Path("/vd0062")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0062Endpoint extends BaseEndpoint<Vd0062InitRequest> {

	@Inject
	private Vd0062Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0062Response init(Vd0062InitRequest req) {
		return service.init(req);
	}

	/**
	 * パーツ選択肢マスタ更新.
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0062Response update(Vd0062SaveRequest req) {
		return service.update(req);
	}

	/**
	 * パーツ選択肢項目マスタ登録.
	 */
	@POST
	@Path("/regist")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0062Response regist(Vd0062SaveRequest req) {
		return service.regist(req);
	}
}
