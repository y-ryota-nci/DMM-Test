package jp.co.nci.iwf.endpoint.wl.wl0130;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ワークリストEndpoint
 */
@Path("/wl0130")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0130Endpoint extends BaseEndpoint<Wl0130Request>{
	@Inject
	private Wl0130Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wl0130Response init(Wl0130Request req) {
		return service.init(req);
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse getList(Wl0130Request req) {
		if ("2".equals(req.trayType)) {
			return service.getOwnProcessList(req);
		} else if ("3".equals(req.trayType)) {
			return service.getAllProcessList(req);
		}
		return service.getWorklist(req);
	}

	/**
	 * 検索条件・一覧表示項目取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initUserDispInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wl0130Response getUserDispInfo(Wl0130Request req) {
		return service.getUserDispInfo(req);
	}
}
