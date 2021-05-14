package jp.co.nci.iwf.endpoint.wm.wm0200;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 参加者ロール一覧のサービス
 */
@BizLogic
public class Wm0200Service extends MmBaseService<WfmAssignRole> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0200Response init(Wm0200Request req) {
		final Wm0200Response res = createResponse(Wm0200Response.class, req);
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
	public Wm0200Response search(Wm0200Request req) {
		SearchWfmAssignRoleInParam inParam = new SearchWfmAssignRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setAssignRoleCode(req.assignRoleCode);
		inParam.setAssignRoleName(req.assignRoleName);
		inParam.setValidStartDate(req.validStartDate);
		inParam.setValidEndDate(req.validEndDate);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setOrderBy(toOrderBy(req, ""));
		inParam.setPageNo(req.pageNo);
		inParam.setPageSize(req.pageSize);
		SearchWfmAssignRoleOutParam outParam = wf.searchWfmAssignRole(inParam);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Wm0200Response res = createResponse(Wm0200Response.class, req, outParam.getCount());
		res.allCount = outParam.getCount();
		res.results = outParam.getAssignRoles();
		res.success = true;
		return res;
	}

	/**
	 * 参加者ロールマスタ追加
	 * @param req
	 * @return
	 */
	public Wm0200Response add(Wm0200Request req) {
		final WfmAssignRole assignRole = new WfmAssignRole();
		assignRole.setCorporationCode(req.corporationCode);
		assignRole.setValidStartDate(today());
		assignRole.setValidEndDate(ENDDATE);
		assignRole.setDeleteFlag(DeleteFlag.OFF);

		final Wm0200Response res = createResponse(Wm0200Response.class, req);
		res.assignRole = assignRole;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.assignRole));
		return res;
	}
}
