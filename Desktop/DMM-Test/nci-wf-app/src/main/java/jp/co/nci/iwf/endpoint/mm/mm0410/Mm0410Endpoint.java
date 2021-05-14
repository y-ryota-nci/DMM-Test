package jp.co.nci.iwf.endpoint.mm.mm0410;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000AddRequest;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000Service;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000UpdateResponse;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 企業一覧Endpoint
 */
@Endpoint
@Path("/mm0410")
@RequiredLogin
@WriteAccessLog
public class Mm0410Endpoint extends BaseEndpoint<Mm0410Request> {

	@Inject private Mm0410Service service;
	@Inject private Mm0000Service mm0000service;

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
	public Mm0410Response init(Mm0410Request req) {
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
	public Mm0410Response search(Mm0410Request req) {
		return service.search(req);
	}

	/**
	 * 企業の追加
	 * @param req
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0000UpdateResponse add(Mm0000AddRequest req) {
		return mm0000service.addCorp(req);
	}
}
