package jp.co.nci.iwf.endpoint.cm.cm0020;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmOrganizationOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jpa.entity.wm.WfvOrganization;

/**
 * 組織選択サービス
 */
@BizLogic
public class Cm0020Service extends CmBaseService<WfvOrganization> {
	@Inject private WfInstanceWrapper wf;
	@Inject private WfmLookupService lookup;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0020Response init(Cm0020Request req) {
		final Cm0020Response res = createResponse(Cm0020Response.class, req);
		// 削除区分
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0020Response search(Cm0020Request req) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setOrganizationAddedInfo(req.organizationAddedInfo);
		in.setOrganizationLevel(req.organizationLevel);
		in.setOrganizationName(req.organizationName);
		in.setOrganizationTreeName(req.organizationTreeName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(toOrderBy(req, "A."));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// 所有ロールによる暗黙の絞り込み条件
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (!login.isAspAdmin()) {
			if (isNotEmpty(login.getCorporationGroupCode()))
				// 企業グループに属していれば、グループ内は見える
				in.setCorporationGroupCode(login.getCorporationGroupCode());
			else
				// ASP管理者でも企業グループにも属してなければ、表示可能なのは自社のみ。
				in.setCorporationCode(login.getCorporationCode());
		}

		final SearchWfmOrganizationOutParam out = wf.searchWfmOrganization(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Cm0020Response res = createResponse(Cm0020Response.class, req, out.getCount());
		res.results = out.getOrganizationList();
		res.success = true;

		return res;
	}

	public WfcOrganization getWfmOrganization(String corporationCode, String organizationCode) {
		SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		return list.isEmpty() ? null : list.get(0);
	}

}
