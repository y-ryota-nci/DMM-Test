package jp.co.nci.iwf.endpoint.rm.rm0903;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmMenuRoleDetailOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ASP管理者ロール構成設定サービス
 */
@BizLogic
public class Rm0903Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0903Response init(Rm0903Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.menuRoleCode))
			throw new BadRequestException("企業コードが未指定です");

		final Rm0903Response res = createResponse(Rm0903Response.class, req);

		if (CommonUtil.isEmpty(req.seqNoMenuRoleDetail)) {
			SearchWfmMenuRoleInParam sarIn = new SearchWfmMenuRoleInParam();
			sarIn.setCorporationCode(req.corporationCode);
			sarIn.setMenuRoleCode(req.menuRoleCode);
			sarIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmMenuRoleOutParam sarOut = wf.searchWfmMenuRole(sarIn);
			WfmMenuRole menuRole = sarOut.getMenuRoles().get(0);

			res.menuRoleDetail = new WfmMenuRoleDetail();
			res.menuRoleDetail.setCorporationCode(req.corporationCode);
			res.menuRoleDetail.setMenuRoleCode(req.menuRoleCode);
			res.menuRoleDetail.setCorporationCodeAccess(req.corporationCode);
			res.menuRoleDetail.setValidStartDate(today());
			res.menuRoleDetail.setValidEndDate(ENDDATE);
			res.menuRoleDetail.setDeleteFlag(DeleteFlag.OFF);
			res.menuRoleDetail.setMenuRoleName(menuRole.getMenuRoleName());
		} else {
			// ASP管理者ロール構成取得
			SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
			sardIn.setCorporationCode(req.corporationCode);
			sardIn.setMenuRoleCode(req.menuRoleCode);
			sardIn.setSeqNoMenuRoleDetail(req.seqNoMenuRoleDetail);
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);
			res.menuRoleDetail = sardOut.getMenuRoleDetails().get(0);
		}

		// システムロール指定方式の選択肢
		res.menuRoleAssignmentTypeList = lookupService.getOptionItems(false, LookupTypeCode.MENU_ROLE_ASSIGNMENT_TYPE);
		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.menuRoleDetail != null);
		return res;
	}

	/**
	 * ASP管理者ロール構成登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0903Response insert(Rm0903InsertRequest req) {
		final Rm0903Response res = createResponse(Rm0903Response.class, req);

		String error = null;
		if (!CommonUtil.isEmpty(req.menuRoleDetail.getSeqNoMenuRoleDetail())) {
			// ASP管理者ロール構成取得
			SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
			sardIn.setCorporationCode(req.menuRoleDetail.getCorporationCode());
			sardIn.setMenuRoleCode(req.menuRoleDetail.getMenuRoleCode());
			sardIn.setSeqNoMenuRoleDetail(req.menuRoleDetail.getSeqNoMenuRoleDetail());
			sardIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

			if (!CommonUtil.isEmpty(sardOut.getMenuRoleDetails())) {
				error = i18n.getText(MessageCd.MSG0108, MessageCd.menuRoleDetail, req.menuRoleDetail.getSeqNoMenuRoleDetail());
			}
		}
		if (error == null)
			error = validate(req.menuRoleDetail);

		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		InsertWfmMenuRoleDetailInParam insertIn = new InsertWfmMenuRoleDetailInParam();
		insertIn.setWfmMenuRoleDetail(req.menuRoleDetail);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertWfmMenuRoleDetail(insertIn);

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.menuRoleDetail.getCorporationCode());
		sardIn.setMenuRoleCode(req.menuRoleDetail.getMenuRoleCode());
		sardIn.setSeqNoMenuRoleDetail(req.menuRoleDetail.getSeqNoMenuRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

		res.menuRoleDetail = sardOut.getMenuRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.menuRoleDetail));
		res.success = true;
		return res;
	}

	/** バリデーション */
	private String validate(WfmMenuRoleDetail d) {
		// 企業が入力されているか
		if (isEmpty(d.getCorporationCodeAccess())) {
			return i18n.getText(MessageCd.MSG0003, MessageCd.corporation);
		}
		else {
			// 企業が存在するか
			final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
			in.setCorporationCode(d.getCorporationCodeAccess());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmCorporation(in).getCorporations().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.corporation);
		}

		// 企業＋組織の組み合わせが存在するか
		if (isNotEmpty(d.getOrganizationCodeAccess())) {
			final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
			in.setCorporationCode(d.getCorporationCodeAccess());
			in.setOrganizationCode(d.getOrganizationCodeAccess());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmOrganization(in).getOrganizationList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.organization);
		}
		// 企業＋役職の組み合わせが存在するか
		if (isNotEmpty(d.getPostCodeAccess())) {
			final SearchWfmPostInParam in = new SearchWfmPostInParam();
			in.setCorporationCode(d.getCorporationCodeAccess());
			in.setPostCode(d.getPostCodeAccess());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmPost(in).getPostList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.post);
		}
		// 企業＋ユーザの組み合わせが存在するか
		if (isNotEmpty(d.getUserCodeAccess())) {
			final SearchWfmUserInParam in = new SearchWfmUserInParam();
			in.setCorporationCode(d.getCorporationCodeAccess());
			in.setUserCode(d.getUserCodeAccess());
			in.setDeleteFlag(DeleteFlag.OFF);
			if (wf.searchWfmUser(in).getUserList().isEmpty())
				return i18n.getText(MessageCd.MSG0156, MessageCd.user);
		}
		// 論理削除後、有効なレコードが1件以上存在するか
		if (DeleteFlag.ON.equals(d.getDeleteFlag())) {
			SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
			sardIn.setCorporationCode(d.getCorporationCode());
			sardIn.setMenuRoleCode(d.getMenuRoleCode());
			SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);
			if (sardOut.getMenuRoleDetails().size() <= 1) {
				return i18n.getText(MessageCd.MSG0207, MessageCd.user);
			}
		}
		return null;
	}

	@Transactional
	public Rm0903Response update(Rm0903InsertRequest req) {
		final Rm0903Response res = createResponse(Rm0903Response.class, req);

		// バリデーション
		String error = validate(req.menuRoleDetail);
		if (!isEmpty(error)) {
			res.addAlerts(error);
			res.success = false;
			return res;
		}

		UpdateWfmMenuRoleDetailInParam updateIn = new UpdateWfmMenuRoleDetailInParam();
		updateIn.setWfmMenuRoleDetail(req.menuRoleDetail);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmMenuRoleDetailOutParam insertOut = wf.updateWfmMenuRoleDetail(updateIn);

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.menuRoleDetail.getCorporationCode());
		sardIn.setMenuRoleCode(req.menuRoleDetail.getMenuRoleCode());
		sardIn.setSeqNoMenuRoleDetail(insertOut.getWfmMenuRoleDetail().getSeqNoMenuRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		sardIn.setDeleteFlag(req.menuRoleDetail.getDeleteFlag());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

		res.menuRoleDetail = sardOut.getMenuRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.menuRoleDetail));
		res.success = true;
		return res;
	}

	@Transactional
	public Rm0903Response delete(Rm0903InsertRequest req) {
		final Rm0903Response res = createResponse(Rm0903Response.class, req);

		DeleteWfmMenuRoleDetailInParam deleteIn = new DeleteWfmMenuRoleDetailInParam();
		deleteIn.setWfmMenuRoleDetail(req.menuRoleDetail);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmMenuRoleDetail(deleteIn);

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.menuRoleDetail.getCorporationCode());
		sardIn.setMenuRoleCode(req.menuRoleDetail.getMenuRoleCode());
		sardIn.setSeqNoMenuRoleDetail(req.menuRoleDetail.getSeqNoMenuRoleDetail());
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

		res.menuRoleDetail = sardOut.getMenuRoleDetails().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.menuRoleDetail));
		res.success = true;
		return res;
	}

}
