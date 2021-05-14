package jp.co.nci.iwf.endpoint.cm.cm0020;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.YmdFormatter;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000Service;
import jp.co.nci.iwf.endpoint.mm.mm0000.Mm0000TreeItem;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 組織選択Endpoint
 */
@Path("/cm0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Cm0020Endpoint extends BaseEndpoint<Cm0020Request> {
	@Inject private Cm0020Service service;
	@Inject private YmdFormatter ymd;
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
	public Cm0020Response init(Cm0020Request req) {
		return service.init(req);
	}


	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cm0020Response getWorklist(Cm0020Request req) {
		return service.search(req);
	}


	/**
	 * （組織ツリー）対象組織の直下組織を返す
	 */
	@GET
	@Path("/getOrgTreeItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Mm0000TreeItem> getOrgTreeItem(
			@QueryParam("nodeId") String nodeId,
			@QueryParam("baseDate") String baseDate,
			@QueryParam("displayValidOnly") Boolean displayValidOnly) {

		return mm0000service.getOrgTreeItem(nodeId, ymd.parseSql(baseDate), displayValidOnly);
	}

	/**
	 * 組織マスタを返す
	 * @param corporationCode
	 * @param organizationCode
	 * @return
	 */
	@GET
	@Path("/getWfmOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WfcOrganization getWfmOrganization(
			@QueryParam("corporationCode") String corporationCode,
			@QueryParam("organizationCode") String organizationCode) {
		return service.getWfmOrganization(corporationCode, organizationCode);
	}
}
