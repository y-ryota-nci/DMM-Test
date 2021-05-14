package jp.co.nci.iwf.endpoint.wm.wm0330;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmChangeRoleDetailOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 参加者変更ロール構成設定サービス
 */
@BizLogic
public class Wm0330Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0330Response init(Wm0330Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.changeRoleCode))
			throw new BadRequestException("参加者変更ロールコードが未指定です");

		final Wm0330Response res = createResponse(Wm0330Response.class, req);

		if (CommonUtil.isEmpty(req.seqNoChangeRoleDetail)) {
			// 参加者ロール
			SearchWfmChangeRoleInParam sarIn = new SearchWfmChangeRoleInParam();
			sarIn.setCorporationCode(req.corporationCode);
			sarIn.setChangeRoleCode(req.changeRoleCode);
			sarIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmChangeRoleOutParam sarOut = wf.searchWfmChangeRole(sarIn);
			WfmChangeRole changeRole = sarOut.getChangeRoles().get(0);

			// 参加者ロール構成
			res.changeRoleDetail = new WfmChangeRoleDetail();
			res.changeRoleDetail.setCorporationCode(changeRole.getCorporationCode());
			res.changeRoleDetail.setChangeRoleCode(changeRole.getChangeRoleCode());
			res.changeRoleDetail.setChangeRoleName(changeRole.getChangeRoleName());
			res.changeRoleDetail.setCorporationCodeAssigned(changeRole.getCorporationCode());
			res.changeRoleDetail.setCorporationNameAssigned(changeRole.getCorporationName());
			res.changeRoleDetail.setValidStartDate(today());
			res.changeRoleDetail.setValidEndDate(ENDDATE);
			res.changeRoleDetail.setDeleteFlag(DeleteFlag.OFF);
			res.changeRoleDetail.setChangeRoleName(changeRole.getChangeRoleName());
		} else {
			// 参加者変更ロール構成取得
			SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
			sardIn.setCorporationCode(req.corporationCode);
			sardIn.setChangeRoleCode(req.changeRoleCode);
			sardIn.setSeqNoChangeRoleDetail(req.seqNoChangeRoleDetail);
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);
			res.changeRoleDetail = sardOut.getChangeRoleDetails().get(0);
		}

		// 参加者変更ロール指定区分の選択肢
		res.changeRoleAssignmentTypeList = lookupService.getOptionItems(false, LookupTypeCode.CHANGE_ROLE_ASSIGNMENT_TYPE);
		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.changeRoleDetail != null);
		return res;
	}

	/**
	 * 参加者変更ロール構成登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0330Response insert(Wm0330InsertRequest req) {
		final Wm0330Response res = createResponse(Wm0330Response.class, req);

		String error = null;
		if (!CommonUtil.isEmpty(req.changeRoleDetail.getSeqNoChangeRoleDetail())) {
			// 参加者変更ロール構成取得
			SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
			sardIn.setCorporationCode(req.changeRoleDetail.getCorporationCode());
			sardIn.setChangeRoleCode(req.changeRoleDetail.getChangeRoleCode());
			sardIn.setSeqNoChangeRoleDetail(req.changeRoleDetail.getSeqNoChangeRoleDetail());
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);

			if (!CommonUtil.isEmpty(sardOut.getChangeRoleDetails())) {
				error = i18n.getText(MessageCd.MSG0108, MessageCd.changeRoleDetail, req.changeRoleDetail.getSeqNoChangeRoleDetail());
			}
		}

		if (error == null)
			error = validate(req.changeRoleDetail);

		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		InsertWfmChangeRoleDetailInParam insertIn = new InsertWfmChangeRoleDetailInParam();
		insertIn.setWfmChangeRoleDetail(req.changeRoleDetail);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertWfmChangeRoleDetail(insertIn);

		// 参加者変更ロール構成取得
		SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
		sardIn.setCorporationCode(req.changeRoleDetail.getCorporationCode());
		sardIn.setChangeRoleCode(req.changeRoleDetail.getChangeRoleCode());
		sardIn.setSeqNoChangeRoleDetail(req.changeRoleDetail.getSeqNoChangeRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);

		res.changeRoleDetail = sardOut.getChangeRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.changeRoleDetail));
		res.success = true;
		return res;
	}

	/** バリデーション */
	private String validate(WfmChangeRoleDetail d) {
		// 企業が入力されているか
		if (isEmpty(d.getCorporationCodeAssigned())) {
			return i18n.getText(MessageCd.MSG0003, MessageCd.corporation);
		}
		else {
			// 企業が存在するか
			final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
			in.setCorporationCode(d.getCorporationCodeAssigned());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmCorporation(in).getCorporations().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.corporation);
		}

		// 企業＋組織の組み合わせが存在するか
		if (isNotEmpty(d.getOrganizationCodeAssigned())) {
			final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
			in.setCorporationCode(d.getCorporationCodeAssigned());
			in.setOrganizationCode(d.getOrganizationCodeAssigned());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmOrganization(in).getOrganizationList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.organization);
		}
		// 企業＋役職の組み合わせが存在するか
		if (isNotEmpty(d.getPostCodeAssigned())) {
			final SearchWfmPostInParam in = new SearchWfmPostInParam();
			in.setCorporationCode(d.getCorporationCodeAssigned());
			in.setPostCode(d.getPostCodeAssigned());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmPost(in).getPostList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.post);
		}
		// 企業＋ユーザの組み合わせが存在するか
		if (isNotEmpty(d.getUserCodeAssigned())) {
			final SearchWfmUserInParam in = new SearchWfmUserInParam();
			in.setCorporationCode(d.getCorporationCodeAssigned());
			in.setUserCode(d.getUserCodeAssigned());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmUser(in).getUserList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.user);
		}
		return null;
	}

	@Transactional
	public Wm0330Response update(Wm0330InsertRequest req) {
		final Wm0330Response res = createResponse(Wm0330Response.class, req);

		String error = validate(req.changeRoleDetail);
		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		UpdateWfmChangeRoleDetailInParam updateIn = new UpdateWfmChangeRoleDetailInParam();
		updateIn.setWfmChangeRoleDetail(req.changeRoleDetail);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmChangeRoleDetailOutParam insertOut = wf.updateWfmChangeRoleDetail(updateIn);

		// 参加者変更ロール構成取得
		SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
		sardIn.setCorporationCode(req.changeRoleDetail.getCorporationCode());
		sardIn.setChangeRoleCode(req.changeRoleDetail.getChangeRoleCode());
		sardIn.setSeqNoChangeRoleDetail(insertOut.getWfmChangeRoleDetail().getSeqNoChangeRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);

		res.changeRoleDetail = sardOut.getChangeRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.changeRoleDetail));
		res.success = true;
		return res;
	}

	@Transactional
	public Wm0330Response delete(Wm0330InsertRequest req) {
		final Wm0330Response res = createResponse(Wm0330Response.class, req);

		DeleteWfmChangeRoleDetailInParam deleteIn = new DeleteWfmChangeRoleDetailInParam();
		deleteIn.setWfmChangeRoleDetail(req.changeRoleDetail);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmChangeRoleDetail(deleteIn);

		// 参加者変更ロール構成取得
		SearchWfmChangeRoleDetailInParam sardIn = new SearchWfmChangeRoleDetailInParam();
		sardIn.setCorporationCode(req.changeRoleDetail.getCorporationCode());
		sardIn.setChangeRoleCode(req.changeRoleDetail.getChangeRoleCode());
		sardIn.setSeqNoChangeRoleDetail(req.changeRoleDetail.getSeqNoChangeRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleDetailOutParam sardOut = wf.searchWfmChangeRoleDetail(sardIn);

		res.changeRoleDetail = sardOut.getChangeRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.changeRoleDetail));
		res.success = true;
		return res;
	}

}
