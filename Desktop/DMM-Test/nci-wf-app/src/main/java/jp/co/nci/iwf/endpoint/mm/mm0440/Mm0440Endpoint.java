package jp.co.nci.iwf.endpoint.mm.mm0440;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000AddUserRequest;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000Service;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000UpdateResponse;
import jp.co.nci.iwf.endpoint.mm.mm0003.Mm0003Service;
import jp.co.nci.iwf.endpoint.mm.mm0003.Mm0003UpdateUserRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ユーザ一覧Endpoint
 */
@Path("/mm0440")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0440Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Mm0440Service service;
	@Inject private Mm0000Service mm0000service;
	@Inject private Mm0003Service mm0003service;

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
	public Mm0440Response search(Mm0440Request req) {
		return service.search(req);
	}

	/**
	 * 指定組織の配下にユーザを追加する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0000UpdateResponse addUser(Mm0000AddUserRequest req) {
		WfmOrganization org = service.getTopOrganization(req.corporationCode);
		req.corporationCode = org.getCorporationCode();
		req.organizationCode = org.getOrganizationCode();
		req.baseDate = today();
		return mm0000service.addUser(req);
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
	public BaseResponse delete(Mm0003UpdateUserRequest req) {
		return mm0003service.delete(req);
	}
}
