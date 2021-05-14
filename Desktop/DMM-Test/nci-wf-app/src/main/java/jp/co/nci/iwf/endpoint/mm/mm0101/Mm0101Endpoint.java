package jp.co.nci.iwf.endpoint.mm.mm0101;

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
 * 情報共有者定義作成Endpoint
 */
@Path("/mm0101")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0101Endpoint extends BaseEndpoint<Mm0101InitRequest>{

	@Inject
	private Mm0101Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0101InitResponse init(Mm0101InitRequest req) {
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
	public Mm0101SearchResponse search(Mm0101SearchRequest req) {
		return service.search(req);
	}


	/**
	 * 作成
	 * @param req
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0101SaveResponse create(Mm0101SaveRequest req) {
		return service.create(req);
	}

}
