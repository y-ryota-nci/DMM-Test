package jp.co.nci.iwf.endpoint.cm.cm0100;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.custom.WfcChangeRoleUser;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.param.input.SearchChangeRoleUserListInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchChangeRoleUserListOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeDefOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 *参加者変更ユーザ検索サービス
 */
@BizLogic
public class Cm0100Service extends CmBaseService<WfcChangeRoleUser> {
	@Inject
	private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Cm0100Request req) {
		final Cm0100Response res = createResponse(Cm0100Response.class, req);

		SearchWfmChangeDefInParam in = new SearchWfmChangeDefInParam();
		in.setCorporationCode(req.corporationCode);
		in.setProcessDefCode(req.processDefCode);
		in.setProcessDefDetailCode(req.processDefDetailCode);
		in.setActivityDefCode(req.activityDefCode);
		in.setDeleteFlag(CommonFlag.OFF);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeDefOutParam out = wf.searchWfmChangeDef(in);

		final List<OptionItem> changeDefOptions = new ArrayList<>();
		for (WfmChangeDef changeDef : out.getChangeDefs()) {
			changeDefOptions.add(new OptionItem(changeDef.getChangeRoleCode(), changeDef.getChangeRoleName()));
		}
		res.changeDefList = changeDefOptions;

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0100Response search(Cm0100Request req) {
		// 検索条件の生成
		SearchChangeRoleUserListInParam in = new SearchChangeRoleUserListInParam();
		in.setCorporationCode(req.corporationCode);
		in.setChangeRoleCode(req.changeRoleCode);
//		in.setOrderBy(toOrderBy(req, "A."));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		SearchChangeRoleUserListOutParam out = wf.searchChangeRoleUserList(in);

		final List<WfcChangeRoleUser> users = out.getChangeRoleUserList();
		int allCount = out.getCount().intValue();

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Cm0100Response res = createResponse(Cm0100Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		res.results = users;
		res.success = true;
		return res;
	}

}
