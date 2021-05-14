package jp.co.nci.iwf.endpoint.wm.wm0320;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 参加者変更ロール設定サービス
 */
@BizLogic
public class Wm0320Service extends MmBaseService<WfmChangeRoleDetail> {

	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0320Response init(Wm0320Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.changeRoleCode))
			throw new BadRequestException("企業コードまたは参加者変更ロールコードが未指定です");

		final Wm0320Response res = createResponse(Wm0320Response.class, req);

		// 参加者変更ロール取得
		SearchWfmChangeRoleInParam sarIn = new SearchWfmChangeRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setChangeRoleCode(req.changeRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleOutParam sarOut = wf.searchWfmChangeRole(sarIn);
		res.changeRole = sarOut.getChangeRoles().get(0);

		// 参加者変更ロール構成取得
		SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setChangeRoleCode(req.changeRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleDetailOutParam
		sardOut = wf.searchWfmChangeRoleDetail(sardIn);
		res.changeRoleDetailList = sardOut.getChangeRoleDetails();

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.changeRole != null);
		return res;
	}

	public Wm0320Response search(Wm0320Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.changeRoleCode)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		// 参加者変更ロール取得
		SearchWfmChangeRoleInParam sarIn = new SearchWfmChangeRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setChangeRoleCode(req.changeRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		WfmChangeRole changeRole = wf.searchWfmChangeRole(sarIn).getChangeRoles().get(0);

		// 参加者変更ロール構成取得
		SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setChangeRoleCode(req.changeRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		sardIn.setOrderBy(toOrderBy(req, ""));
		sardIn.setPageNo(req.pageNo);
		sardIn.setPageSize(req.pageSize);
		SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Wm0320Response res = createResponse(Wm0320Response.class, req, sardOut.getCount());
		res.results = sardOut.getChangeRoleDetails();
		res.changeRole = changeRole;

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
	public Wm0320Response update(Wm0320UpdateRequest req) {
		// 参加者変更ロール取得
		SearchWfmChangeRoleInParam sarIn = new SearchWfmChangeRoleInParam();
		sarIn.setCorporationCode(req.changeRole.getCorporationCode());
		sarIn.setChangeRoleCode(req.changeRole.getChangeRoleCode());
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleOutParam sarOut = wf.searchWfmChangeRole(sarIn);

		if (CommonUtil.isEmpty(sarOut.getChangeRoles())) {
			throw new AlreadyUpdatedException();
		}
		WfmChangeRole changeRole = sarOut.getChangeRoles().get(0);
		changeRole.setChangeRoleName(req.changeRole.getChangeRoleName());
		changeRole.setValidStartDate(req.changeRole.getValidStartDate());
		changeRole.setValidEndDate(req.changeRole.getValidEndDate());
		changeRole.setDeleteFlag(req.changeRole.getDeleteFlag());
		changeRole.setTimestampUpdated(req.changeRole.getTimestampUpdated());

		UpdateWfmChangeRoleInParam updateIn = new UpdateWfmChangeRoleInParam();
		updateIn.setWfmChangeRole(changeRole);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmChangeRole(updateIn);

		final Wm0320Response res = createResponse(Wm0320Response.class, req);

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.changeRole));
		res.success = true;
		return res;
	}

	/**
	 * ルックアップの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0320Response delete(Wm0320Request req) {
		for (WfmChangeRoleDetail deleteChangeRoleDetail : req.deleteChangeRoleDetails) {
			DeleteWfmChangeRoleDetailInParam deleteIn = new DeleteWfmChangeRoleDetailInParam();
			deleteIn.setWfmChangeRoleDetail(deleteChangeRoleDetail);
			deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
			wf.deleteWfmChangeRoleDetail(deleteIn);
		}
		final Wm0320Response res = createResponse(Wm0320Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.changeRoleDetail));

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

}
