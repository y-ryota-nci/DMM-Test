package jp.co.nci.iwf.endpoint.ws.ws0011;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.ws.WsSaveRequest;
import jp.co.nci.iwf.endpoint.ws.WsSaveResponse;
import jp.co.nci.iwf.endpoint.ws.WsSaveService;
import jp.co.nci.iwf.endpoint.ws.WsPersistenceRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 代理設定Endpoint.
 */
@Endpoint
@Path("/ws0011")
@RequiredLogin
@WriteAccessLog
public class Ws0011Endpoint extends BaseEndpoint<WsSaveRequest> {

	@Inject
	private WsSaveService service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(WsSaveRequest req) {
		return service.init(req);
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Path("/regist")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSaveResponse regist(WsPersistenceRequest req) {
		return service.regist(req);
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSaveResponse update(WsPersistenceRequest req) {
		return service.update(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WsSaveResponse delete(WsPersistenceRequest req) {
		return service.delete(req);
	}
}
