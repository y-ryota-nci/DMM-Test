package jp.co.nci.iwf.endpoint.cm.cm0010;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 企業選択画面サービス
 */
@BizLogic
public class Cm0010Service extends BasePagingService {
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0010Response init(Cm0010Request req) {
		Cm0010Response res = createResponse(Cm0010Response.class, req);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(true);
		// 企業グループの選択肢
		res.corporationGroups = corp.getMyCorporationGroup(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0010Response search(Cm0010Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setCorporationGroupCode(req.corporationGroupCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(toOrderBy(req, "A."));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);

		// 所有ロールによる暗黙の絞り込み条件
		if (!login.isAspAdmin()) {
			if (isNotEmpty(login.getCorporationGroupCode()))
				// 企業グループに属していれば、グループ内は見える
				in.setCorporationGroupCode(login.getCorporationGroupCode());
			else
				// ASP管理者でも企業グループにも属してなければ、表示可能なのは自社のみ。
				in.setCorporationCode(login.getCorporationCode());
		}
		final SearchWfmCorporationOutParam out = wf.searchWfmCorporation(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		int allCount = out.getCount();
		final Cm0010Response res = createResponse(Cm0010Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		res.results = out.getCorporations();
		res.success = true;
		return res;
	}
}
