package jp.co.nci.iwf.endpoint.cm.cm0210;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.document.DocInfoSearchRequest;
import jp.co.nci.iwf.component.document.DocInfoSearchResponse;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書情報選択Endpoint.
 */
@Endpoint
@Path("/cm0210")
@RequiredLogin
@WriteAccessLog
public class Cm0210Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject
	private Cm0210Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Cm0210InitResponse init(BaseRequest req) {
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
	public DocInfoSearchResponse search(DocInfoSearchRequest req) {
		return service.search(req);
	}
}
