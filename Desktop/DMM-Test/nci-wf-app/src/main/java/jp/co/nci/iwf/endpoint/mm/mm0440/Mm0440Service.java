package jp.co.nci.iwf.endpoint.mm.mm0440;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmUserOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ユーザ一覧画面サービス
 */
@BizLogic
public class Mm0440Service extends BasePagingService {
	/** ルックアップサービス */
	@Inject private WfmLookupService lookup;
	@Inject private LocaleService locale;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** WF API */
	@Inject private WfInstanceWrapper wf;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Mm0440InitResponse res = createResponse(Mm0440InitResponse.class, req);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// 言語コード
		res.localeCodes = locale.getSelectableLocaleCodeOptions(true);
		// 削除区分
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0440Response search(Mm0440Request req) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(req.corporationCode);
		in.setDeleteFlag(req.deleteFlag);
		in.setMailAddress(req.mailAddress);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setUserAddedInfo(req.userAddedInfo);
		in.setUserName(req.userName);
		in.setTelNum(req.telNum);
		in.setTelNumCel(req.telNumCel);
		in.setDefaultLocaleCode(req.defaultLocaleCode);
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		in.setOrderBy(toOrderBy(req, ""));
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		final SearchWfmUserOutParam out = wf.searchWfmUser(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0440Response res = createResponse(Mm0440Response.class, req, out.getCount());

		res.results = out.getUserList();
		res.success = true;
		return res;
	}

	/**
	 * 対象企業の第一階層組織を抽出
	 * @param corporationCode
	 * @return
	 */
	public WfmOrganization getTopOrganization(String corporationCode) {
		SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A.CORPORATION_CODE"),
				new OrderBy(true, "A.ORGANIZATION_CODE"),
		});
		List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		return list.isEmpty() ? null : list.get(0);
	}
}
