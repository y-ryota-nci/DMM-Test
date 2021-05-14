package jp.co.dmm.customize.endpoint.bd.bd0808;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Repository;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 特定組織指定バージョン予算／実績分析画面サービス
 */
@BizLogic
public class Bd0808Service extends BasePagingService {
	@Inject private Bd0808Repository repository;
	@Inject private Bd0801Repository bd0801;
	@Inject private MwmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Bd0808InitResponse init(Bd0808InitRequest req) {
		final Bd0808InitResponse res = createResponse(Bd0808InitResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();
		res.years = bd0801.getYearList();
		res.yrCd = req.yrCd;
		res.orgLv2s = bd0801.getOrgLv2List(login.getCorporationCode());
		res.organizationCodeLv2 = req.organizationCodeLv2;
		res.orgLv3s = bd0801.getOrgLv3List(login.getCorporationCode(), req.organizationCodeLv2);
		res.organizationCodeLv3 = req.organizationCodeLv3;
		res.rcvCostPayTps = lookup.getOptionItems(LookupGroupId.RCV_COST_PAY_TP, true);
		res.rcvCostPayTp = req.rcvCostPayTp;
		res.hstVersions = repository.getHstVersions(req);
		res.hstVersion = req.hstVersion;
		res.bgtItmCds = repository.getBgtItmCds(login.getCorporationCode());
		res.bsplTps = lookup.getOptionItems(LookupGroupId.BS_PL_TP, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Bd0808SearchResponse search(Bd0808SearchRequest req) {
		final Bd0808SearchResponse res = createResponse(Bd0808SearchResponse.class, req);
		res.results = repository.select(req);
		res.success = true;
		return res;
	}

	/**
	 * 本部変更時
	 * @param req
	 * @return
	 */
	public Bd0808InitResponse changeOrgLevel2(Bd0808InitRequest req) {
		if (isEmpty(req.organizationCodeLv2)) throw new BadRequestException("本部が未指定です");

		final Bd0808InitResponse res = createResponse(Bd0808InitResponse.class, req);
		final LoginInfo login = sessionHolder.getLoginInfo();
		res.orgLv3s = bd0801.getOrgLv3List(login.getCorporationCode(), req.organizationCodeLv2);
		res.hstVersions = repository.getHstVersions(req);
		res.success = true;
		return res;
	}

	/**
	 * 年度／本部／部・室／検収・支払基準／予算科目を変更したことによる履歴バージョンを取得
	 * @param req
	 * @return
	 */
	public Bd0808InitResponse getHstVersion(Bd0808InitRequest req) {
		final Bd0808InitResponse res = createResponse(Bd0808InitResponse.class, req);
		res.hstVersions = repository.getHstVersions(req);
		res.success = true;
		return res;
	}
}
