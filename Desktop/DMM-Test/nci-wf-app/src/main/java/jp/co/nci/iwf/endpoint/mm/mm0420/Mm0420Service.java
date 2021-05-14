package jp.co.nci.iwf.endpoint.mm.mm0420;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmOrganizationOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 組織一覧画面サービス
 */
@BizLogic
public class Mm0420Service extends BasePagingService {
	/** ルックアップサービス */
	@Inject private WfmLookupService lookup;
	/** 言語サービス */
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
		final Mm0420InitResponse res = createResponse(Mm0420InitResponse.class, req);
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
	public Mm0420Response search(Mm0420Request req) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setDeleteFlag(req.deleteFlag);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setOrganizationName(req.organizationName);
		in.setOrganizationAddedInfo(req.organizationAddedInfo);
		in.setOrganizationLevel(req.organizationLevel);
		in.setOrganizationCodeUp(req.organizationCodeUp);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);
		in.setOrderBy(toOrderBy(req, ""));
		final SearchWfmOrganizationOutParam out = wf.searchWfmOrganization(in);

		final List<WfcOrganization> organizationList = out.getOrganizationList();
		final int allCount = out.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0420Response res = createResponse(Mm0420Response.class, req, allCount);

		res.results = organizationList;
		res.success = true;

		return res;
	}
}
