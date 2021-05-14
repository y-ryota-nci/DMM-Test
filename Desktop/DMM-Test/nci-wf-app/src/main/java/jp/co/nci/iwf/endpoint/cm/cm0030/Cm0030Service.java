package jp.co.nci.iwf.endpoint.cm.cm0030;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfcPost;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmPostOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 役職選択サービス
 */
@BizLogic
public class Cm0030Service extends CmBaseService<WfcPost> {
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Cm0030Request req) {
		final Cm0030Response res = createResponse(Cm0030Response.class, req);
		res.success = true;
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(true);
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0030Response search(Cm0030Request req) {
		SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(req.corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setPostCode(req.postCode);
		in.setPostName(req.postName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setOrderBy(toOrderBy(req, "A."));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);

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

		SearchWfmPostOutParam out = wf.searchWfmPost(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Cm0030Response res = createResponse(Cm0030Response.class, req, out.getCount());
		res.results = out.getPostList();
		res.success = true;
		return res;
	}
}
