package jp.co.nci.iwf.endpoint.vd.vd0061;

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
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 選択肢マスタ登録Endpoint.
 */
@Path("/vd0061")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0061Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject
	private Vd0061Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0061InitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/regist")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0061InsertResponse regist(Vd0061InsertRequest req) {
		return service.regist(req);
	}
}
