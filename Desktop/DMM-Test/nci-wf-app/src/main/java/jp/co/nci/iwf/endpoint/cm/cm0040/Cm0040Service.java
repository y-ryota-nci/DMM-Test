package jp.co.nci.iwf.endpoint.cm.cm0040;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmUserOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ユーザ選択サービス
 */
@BizLogic
public class Cm0040Service extends CmBaseService<WfmUser> {
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Cm0040Request req) {
		final Cm0040Response res = createResponse(Cm0040Response.class, req);
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
	public Cm0040Response search(Cm0040Request req) {
		// 検索条件の生成
		SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(req.corporationCode);
		in.setUserAddedInfo(req.userAddedInfo);
		in.setUserName(req.userName);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setOrganizationCode(req.organizationCode);
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
		// 検索実行
		SearchWfmUserOutParam out = wf.searchWfmUser(in);

		final List<WfmUser> users = out.getUserList();
		int allCount = out.getCount().intValue();

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Cm0040Response res = createResponse(Cm0040Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		res.results = users;
		res.success = true;
		return res;
	}

}
