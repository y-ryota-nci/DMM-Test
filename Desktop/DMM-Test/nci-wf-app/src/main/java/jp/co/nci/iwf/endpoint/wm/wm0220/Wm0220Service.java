package jp.co.nci.iwf.endpoint.wm.wm0220;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 参加者ロール設定サービス
 */
@BizLogic
public class Wm0220Service extends MmBaseService<WfmAssignRoleDetail> {

	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0220Response init(Wm0220Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.assignRoleCode))
			throw new BadRequestException("企業コードまたは参加者ロールコードが未指定です");

		final Wm0220Response res = createResponse(Wm0220Response.class, req);

		// 参加者ロール取得
		SearchWfmAssignRoleInParam sarIn = new SearchWfmAssignRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setAssignRoleCode(req.assignRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleOutParam sarOut = wf.searchWfmAssignRole(sarIn);
		res.assignRole = sarOut.getAssignRoles().get(0);

		// 参加者ロール構成取得
		SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setAssignRoleCode(req.assignRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);
		res.assignRoleDetailList = sardOut.getAssignRoleDetails();

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.assignRole != null);
		return res;
	}

	public Wm0220Response search(Wm0220Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.assignRoleCode)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		// 参加者ロール取得
		SearchWfmAssignRoleInParam sarIn = new SearchWfmAssignRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setAssignRoleCode(req.assignRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		WfmAssignRole assignRole = wf.searchWfmAssignRole(sarIn).getAssignRoles().get(0);

		// 参加者ロール構成取得
		SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setAssignRoleCode(req.assignRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		sardIn.setOrderBy(toOrderBy(req, ""));
		sardIn.setPageNo(req.pageNo);
		sardIn.setPageSize(req.pageSize);
		SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Wm0220Response res = createResponse(Wm0220Response.class, req, sardOut.getCount());
		res.results = sardOut.getAssignRoleDetails();
		res.assignRole = assignRole;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;

		return res;
	}

	/**
	 * ルックアップグループの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0220Response update(Wm0220UpdateRequest req) {
		// 参加者ロール取得
		SearchWfmAssignRoleInParam sarIn = new SearchWfmAssignRoleInParam();
		sarIn.setCorporationCode(req.assignRole.getCorporationCode());
		sarIn.setAssignRoleCode(req.assignRole.getAssignRoleCode());
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleOutParam sarOut = wf.searchWfmAssignRole(sarIn);

		if (CommonUtil.isEmpty(sarOut.getAssignRoles())) {
			throw new AlreadyUpdatedException();
		}
		WfmAssignRole assignRole = sarOut.getAssignRoles().get(0);
		assignRole.setAssignRoleName(req.assignRole.getAssignRoleName());
		assignRole.setValidStartDate(req.assignRole.getValidStartDate());
		assignRole.setValidEndDate(req.assignRole.getValidEndDate());
		assignRole.setDeleteFlag(req.assignRole.getDeleteFlag());
		assignRole.setTimestampUpdated(req.assignRole.getTimestampUpdated());

		UpdateWfmAssignRoleInParam updateIn = new UpdateWfmAssignRoleInParam();
		updateIn.setWfmAssignRole(assignRole);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmAssignRole(updateIn);

		final Wm0220Response res = createResponse(Wm0220Response.class, req);

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.assignRole));
		res.success = true;
		return res;
	}

	/**
	 * ルックアップの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0220Response delete(Wm0220Request req) {
		for (WfmAssignRoleDetail deleteAssignRoleDetail : req.deleteAssignRoleDetails) {
			DeleteWfmAssignRoleDetailInParam deleteIn = new DeleteWfmAssignRoleDetailInParam();
			deleteIn.setWfmAssignRoleDetail(deleteAssignRoleDetail);
			deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
			wf.deleteWfmAssignRoleDetail(deleteIn);
		}
		final Wm0220Response res = createResponse(Wm0220Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.assignRoleDetail));

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

}
