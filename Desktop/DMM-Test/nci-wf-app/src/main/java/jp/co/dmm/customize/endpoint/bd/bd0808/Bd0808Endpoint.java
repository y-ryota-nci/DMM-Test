package jp.co.dmm.customize.endpoint.bd.bd0808;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 特定組織指定バージョン予算／実績分析画面Endpoint
 */
@Path("/bd0808")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Bd0808Endpoint extends BaseEndpoint<Bd0808InitRequest> {
	@Inject private Bd0808Service service;
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

	/**
	 * 年度／本部／部・室／検収・支払基準／予算科目を変更したことによる履歴バージョンを取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getHstVersion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bd0808InitResponse getHstVersion(Bd0808InitRequest req) {
		return service.getHstVersion(req);
	}
}
