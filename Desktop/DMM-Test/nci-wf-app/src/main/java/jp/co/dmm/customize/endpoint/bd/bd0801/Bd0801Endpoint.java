package jp.co.dmm.customize.endpoint.bd.bd0801;

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
 * 予算入力画面Endpoint
 */
@Path("/bd0801")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0801Endpoint extends BaseEndpoint<Bd0801InitRequest> {
	@Inject private Bd0801Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Bd0801InitResponse init(Bd0801InitRequest req) {
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
	public Bd0801SearchResponse search(Bd0801SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 本部変更時
	 * @param req
	 * @return
	 */
	@POST
	@Path("/changeOrgLevel2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bd0801InitResponse changeOrgLevel2(Bd0801InitRequest req) {
		return service.changeOrgLevel2(req);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bd0801SearchResponse save(Bd0801SearchRequest req) {
		return service.save(req);
	}
}
