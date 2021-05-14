package jp.co.dmm.customize.endpoint.ss.ss0010;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * SS連携データEndpoint
 */
@Path("/ss0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ss0010Endpoint extends BaseEndpoint<Ss0010Request> {

	@Inject private Ss0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ss0010Response init(Ss0010Request req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ss0010Response search(Ss0010Request req) {
		return service.search(req);
	}

	/**
	 * ダウンロード
	 */
	@POST
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response download(Ss0010Request req) {
		return service.download(req);
	}
}
