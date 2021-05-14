package jp.co.nci.iwf.endpoint.mm.mm0420;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000AddOrgRequest;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000Service;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000UpdateResponse;
import jp.co.nci.iwf.endpoint.mm.mm0002.Mm0002Service;
import jp.co.nci.iwf.endpoint.mm.mm0002.Mm0002UpdateRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 組織一覧画面のEndpoint
 */
@Path("/mm0420")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0420Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Mm0420Service service;
	@Inject private Mm0000Service mm0000service;
	@Inject private Mm0002Service mm0002service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
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
	public Mm0420Response search(Mm0420Request req) {
		return service.search(req);
	}

	/**
	 * 組織を追加する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addOrg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0000UpdateResponse addOrg(Mm0000AddOrgRequest req) {
		req.baseDate = today();
		return mm0000service.addOrg(req);
	}

	/**
	 * 指定組織の配下に組織を追加する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Mm0002UpdateRequest req) {
		return mm0002service.delete(req);
	}

}
