package jp.co.nci.iwf.endpoint.wm.wm0230;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmAssignRoleDetailOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 参加者ロール構成設定サービス
 */
@BizLogic
public class Wm0230Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0230Response init(Wm0230Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(isEmpty(req.assignRoleCode)))
			throw new BadRequestException("参加者ロールコードが未指定です");

		final Wm0230Response res = createResponse(Wm0230Response.class, req);

		if (CommonUtil.isEmpty(req.seqNoAssignRoleDetail)) {
			// 参加者ロール
			SearchWfmAssignRoleInParam sarIn = new SearchWfmAssignRoleInParam();
			sarIn.setCorporationCode(req.corporationCode);
			sarIn.setAssignRoleCode(req.assignRoleCode);
			sarIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmAssignRoleOutParam sarOut = wf.searchWfmAssignRole(sarIn);
			WfmAssignRole assignRole = sarOut.getAssignRoles().get(0);

			// 参加者ロール構成
			res.assignRoleDetail = new WfmAssignRoleDetail();
			copyProperties(assignRole, res.assignRoleDetail);
			res.assignRoleDetail.setCorporationCode(assignRole.getCorporationCode());
			res.assignRoleDetail.setAssignRoleCode(assignRole.getAssignRoleCode());
			res.assignRoleDetail.setAssignRoleName(assignRole.getAssignRoleName());
			res.assignRoleDetail.setCorporationCodeAssigned(assignRole.getCorporationCode());
			res.assignRoleDetail.setCorporationNameAssigned(assignRole.getCorporationName());
			res.assignRoleDetail.setValidStartDate(today());
			res.assignRoleDetail.setValidEndDate(ENDDATE);
			res.assignRoleDetail.setDeleteFlag(DeleteFlag.OFF);
			res.assignRoleDetail.setAssignRoleName(assignRole.getAssignRoleName());
		} else {
			// 参加者ロール構成取得
			SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
			sardIn.setCorporationCode(req.corporationCode);
			sardIn.setAssignRoleCode(req.assignRoleCode);
			sardIn.setSeqNoAssignRoleDetail(req.seqNoAssignRoleDetail);
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);
			res.assignRoleDetail = sardOut.getAssignRoleDetails().get(0);
		}

		// 所属区分の選択肢
		res.belongTypeList = lookupService.getOptionItems(false, LookupTypeCode.BELONG_TYPE);
		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.assignRoleDetail != null);
		return res;
	}

	/**
	 * 参加者ロール構成登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0230Response insert(Wm0230InsertRequest req) {
		final Wm0230Response res = createResponse(Wm0230Response.class, req);

		String error = null;
		if (!CommonUtil.isEmpty(req.assignRoleDetail.getSeqNoAssignRoleDetail())) {
			// 参加者ロール構成取得
			SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
			sardIn.setCorporationCode(req.assignRoleDetail.getCorporationCode());
			sardIn.setAssignRoleCode(req.assignRoleDetail.getAssignRoleCode());
			sardIn.setSeqNoAssignRoleDetail(req.assignRoleDetail.getSeqNoAssignRoleDetail());
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);

			if (!CommonUtil.isEmpty(sardOut.getAssignRoleDetails())) {
				error = i18n.getText(MessageCd.MSG0108, MessageCd.assignRoleDetail, req.assignRoleDetail.getSeqNoAssignRoleDetail());
			}
		}

		if (error == null)
			error = validate(req.assignRoleDetail);

		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		InsertWfmAssignRoleDetailInParam insertIn = new InsertWfmAssignRoleDetailInParam();
		insertIn.setWfmAssignRoleDetail(req.assignRoleDetail);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertWfmAssignRoleDetail(insertIn);

		// 参加者ロール構成取得
		SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
		sardIn.setCorporationCode(req.assignRoleDetail.getCorporationCode());
		sardIn.setAssignRoleCode(req.assignRoleDetail.getAssignRoleCode());
		sardIn.setSeqNoAssignRoleDetail(req.assignRoleDetail.getSeqNoAssignRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);

		res.assignRoleDetail = sardOut.getAssignRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.assignRoleDetail));
		res.success = true;
		return res;
	}

	/** バリデーション */
	private String validate(WfmAssignRoleDetail d) {
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
	public Wm0230Response update(Wm0230InsertRequest req) {
		final Wm0230Response res = createResponse(Wm0230Response.class, req);

		String error = validate(req.assignRoleDetail);
		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		UpdateWfmAssignRoleDetailInParam updateIn = new UpdateWfmAssignRoleDetailInParam();
		updateIn.setWfmAssignRoleDetail(req.assignRoleDetail);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmAssignRoleDetailOutParam insertOut = wf.updateWfmAssignRoleDetail(updateIn);

		// 参加者ロール構成取得
		SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
		sardIn.setCorporationCode(req.assignRoleDetail.getCorporationCode());
		sardIn.setAssignRoleCode(req.assignRoleDetail.getAssignRoleCode());
		sardIn.setSeqNoAssignRoleDetail(insertOut.getWfmAssignRoleDetail().getSeqNoAssignRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);

		res.assignRoleDetail = sardOut.getAssignRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.assignRoleDetail));
		res.success = true;
		return res;
	}

	@Transactional
	public Wm0230Response delete(Wm0230InsertRequest req) {
		final Wm0230Response res = createResponse(Wm0230Response.class, req);

		DeleteWfmAssignRoleDetailInParam deleteIn = new DeleteWfmAssignRoleDetailInParam();
		deleteIn.setWfmAssignRoleDetail(req.assignRoleDetail);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmAssignRoleDetail(deleteIn);

		// 参加者ロール構成取得
		SearchWfmAssignRoleDetailInParam sardIn = new SearchWfmAssignRoleDetailInParam();
		sardIn.setCorporationCode(req.assignRoleDetail.getCorporationCode());
		sardIn.setAssignRoleCode(req.assignRoleDetail.getAssignRoleCode());
		sardIn.setSeqNoAssignRoleDetail(req.assignRoleDetail.getSeqNoAssignRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleDetailOutParam sardOut = wf.searchWfmAssignRoleDetail(sardIn);

		res.assignRoleDetail = sardOut.getAssignRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.assignRoleDetail));
		res.success = true;
		return res;
	}

}
