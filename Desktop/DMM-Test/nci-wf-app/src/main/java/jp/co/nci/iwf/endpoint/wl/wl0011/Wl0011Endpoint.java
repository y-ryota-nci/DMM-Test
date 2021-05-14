package jp.co.nci.iwf.endpoint.wl.wl0011;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * トレイ編集Endpoint
 */
@Path("/wl0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0011Endpoint extends BaseEndpoint<Wl0011InitRequest> {
	@Inject private Wl0011Service service;
	@Context HttpServletRequest http;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Wl0011Response init(Wl0011InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wl0011Response save(Wl0011SaveRequest req) {
		return service.save(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wl0011Response delete(Wl0011SaveRequest req) {
		return service.delete(req);
	}
}
