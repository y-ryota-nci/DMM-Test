package jp.co.nci.iwf.endpoint.wm.wm0300;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 参加者変更ロール一覧のサービス
 */
@BizLogic
public class Wm0300Service extends MmBaseService<WfmChangeRole> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0300Response init(Wm0300Request req) {
		final Wm0300Response res = createResponse(Wm0300Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Wm0300Response search(Wm0300Request req) {
		SearchWfmChangeRoleInParam inParam = new SearchWfmChangeRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setChangeRoleCode(req.changeRoleCode);
		inParam.setChangeRoleName(req.changeRoleName);
		inParam.setValidStartDate(req.validStartDate);
		inParam.setValidEndDate(req.validEndDate);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setOrderBy(toOrderBy(req, ""));
		inParam.setPageNo(req.pageNo);
		inParam.setPageSize(req.pageSize);
		SearchWfmChangeRoleOutParam outParam = wf.searchWfmChangeRole(inParam);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Wm0300Response res = createResponse(Wm0300Response.class, req, outParam.getCount());
		res.allCount = outParam.getCount();
		res.results = outParam.getChangeRoles();
		res.success = true;
		return res;
	}

	/**
	 * 参加者変更ロールマスタ追加
	 * @param req
	 * @return
	 */
	public Wm0300Response add(Wm0300Request req) {
		final WfmChangeRole changeRole = new WfmChangeRole();
		changeRole.setCorporationCode(req.corporationCode);
		changeRole.setValidStartDate(today());
		changeRole.setValidEndDate(ENDDATE);
		changeRole.setDeleteFlag(DeleteFlag.OFF);

		final Wm0300Response res = createResponse(Wm0300Response.class, req);
		res.changeRole = changeRole;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.changeRole));
		return res;
	}
}
