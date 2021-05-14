package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.YmdFormatter;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル管理画面Endpoint
 */
@Path("/mm0000")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0000Endpoint extends BaseEndpoint<Mm0000InitRequest> {
	@Inject
	private Mm0000Service service;
	@Inject
	private YmdFormatter ymd;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0000InitResponse init(Mm0000InitRequest req) {
		return service.init(req);
	}

	/**
	 * 対象組織の直下組織を返す
	 */
	@GET
	@Path("/getOrgTreeItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Mm0000TreeItem> getOrgTreeItem(
			@QueryParam("nodeId") String nodeId,
			@QueryParam("baseDate") String baseDate,
			@QueryParam("displayValidOnly") Boolean displayValidOnly) {

		return service.getOrgTreeItem(nodeId, ymd.parseSql(baseDate), displayValidOnly);
	}

	/**
	 * 対象組織の配下ユーザを返す
	 */
	@GET
	@Path("/getUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Mm0000TreeItem> getUser(
			@QueryParam("nodeId") String nodeId,
			@QueryParam("baseDate") String baseDate,
			@QueryParam("displayValidOnly") Boolean displayValidOnly) {
		return service.getUser(nodeId, ymd.parseSql(baseDate), displayValidOnly);
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
	public BaseResponse addUser(Mm0000AddRequest req) {
		return service.addUser(req);
	}

	/**
	 * 指定組織の配下に組織を追加する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addOrg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse addOrg(Mm0000AddRequest req) {
		return service.addOrg(req);
	}

	/**
	 * 指定組織を削除する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/deleteOrg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse deleteOrg(Mm0000DeleteRequest req) {
		return service.deleteOrg(req);
	}

	/**
	 * 新規に企業を追加する
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addCorp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse addCorp(Mm0000AddRequest req) {
		return service.addCorp(req);
	}
}
