package jp.co.dmm.customize.endpoint.bd.bd0806;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808InitRequest;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808InitResponse;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808Repository;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808SearchRequest;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808SearchResponse;
import jp.co.dmm.customize.jpa.entity.mw.VOrganizationLevel;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 組織横断最新予算／実績分析画面Endpoint
 */
@Path("/bd0806")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0806Endpoint extends BaseEndpoint<Bd0808InitRequest> {
	@Inject private Bd0806Service service;
	@Inject private Bd0808Repository bd0808;
	@Inject private SessionHolder sessionHolder;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Bd0808InitResponse init(Bd0808InitRequest req) {
		req.yrCd = toStr(today(), "yyyyMMdd").substring(0, 4);
		req.rcvCostPayTp = "1";
		List<VOrganizationLevel> orgLvlList = bd0808.getMyOrganizationLevel();
		if(orgLvlList != null) {
			req.organizationCodeLv2 = orgLvlList.get(0).getOrganizationCode2();
			req.organizationCodeLv3 = orgLvlList.get(0).getOrganizationCode3();
		}
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
	public Bd0808SearchResponse search(Bd0808SearchRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		req.companyCd = login.getCorporationCode();
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
	public Bd0808InitResponse changeOrgLevel2(Bd0808InitRequest req) {
		return service.changeOrgLevel2(req);
	}
}
