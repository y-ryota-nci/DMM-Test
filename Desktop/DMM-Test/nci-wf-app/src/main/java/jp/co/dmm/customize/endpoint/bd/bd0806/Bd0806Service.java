package jp.co.dmm.customize.endpoint.bd.bd0806;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Repository;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808InitRequest;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808InitResponse;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808Repository;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808Result;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808SearchRequest;
import jp.co.dmm.customize.endpoint.bd.bd0808.Bd0808SearchResponse;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 組織横断最新予算／実績分析画面サービス
 */
@BizLogic
public class Bd0806Service extends BasePagingService {
	@Inject private Bd0801Repository bd0801;
	@Inject private Bd0808Repository bd0808;
	@Inject private MwmLookupService lookup;
	@Inject private WfInstanceWrapper api;

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
		res.bsplTps = lookup.getOptionItems(LookupGroupId.BS_PL_TP, true);
		res.bsplTp = req.bsplTp;
		res.bgtItmCds = bd0808.getBgtItmCds(login.getCorporationCode());
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Bd0808SearchResponse search(Bd0808SearchRequest req) {
		if (isEmpty(req.yrCd))
			throw new BadRequestException("年度が指定されていません。");
		if (isEmpty(req.rcvCostPayTp))
			throw new BadRequestException("検収基準/支払基準が指定されていません。");
		if (isEmpty(req.organizationCodeLv2))
			throw new BadRequestException("本部が指定されていません。");

		final Bd0808SearchResponse res = createResponse(Bd0808SearchResponse.class, req);

		// 予算科目数
		int bgtItmCount = bd0808.getBgtItmCdsBsPl(req).size() - 1;

		// 本部配下の部・室を求めて、部・室単位で集計する。
		/*
		final List<WfcOrganization> orgLv3List = getOrganizationLv3(req.organizationCodeLv2, req.organizationCodeLv3);
		final List<Bd0808Result> results = new ArrayList<>();
		for (WfcOrganization orgLv3 : orgLv3List) {
			// 部・室単位での集計はBD0808で行っているので、それを使いまわす
			req.organizationCodeLv3 = orgLv3.getOrganizationCode();
			final List<Bd0808Result> resultsByOrgLv3 = bd0808.select(req);
			// 「組織」欄のセル結合用に、rowspanを計算。
			// rowspan=予算科目×4 (予算／実績(COM)／実績(DCM)／差異の４つ分)
			int rowspan = 1 * (isEmpty(req.bgtItmCd) ? bgtItmCount : 1);
			resultsByOrgLv3.get(0).rowspan = rowspan;
			results.addAll(resultsByOrgLv3);
		}
		*/

		final List<Bd0808Result> results = new ArrayList<>();
		// 部・室単位での集計はBD0808で行っているので、それを使いまわす
		final List<Bd0808Result> resultsByOrgLv3 = bd0808.select(req);
		int rowspan = 1 * (isEmpty(req.bgtItmCd) ? bgtItmCount : 1);
		if(resultsByOrgLv3 != null && resultsByOrgLv3.size() > 0) {
			resultsByOrgLv3.get(0).rowspan = rowspan;
		}
		results.addAll(resultsByOrgLv3);

		res.results = results;
		res.success = true;
		return res;
	}

	/** 本部配下の事業部を抽出 */
	private List<WfcOrganization> getOrganizationLv3(String organizationCodeLv2, String organizationCodeLv3) {
		SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(CorporationCodes.DMM_COM);
		in.setOrganizationCode(organizationCodeLv3);
		in.setOrganizationCodeUp(organizationCodeLv2);
		in.setOrganizationLevel(3);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		return api.searchWfmOrganization(in).getOrganizationList();
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
		res.hstVersions = bd0808.getHstVersions(req);
		res.success = true;
		return res;
	}
}
