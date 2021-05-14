package jp.co.nci.iwf.endpoint.rm.rm0902;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleDetailOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * ASP管理者登録サービス
 */
@BizLogic
public class Rm0902Service extends MmBaseService<WfmMenuRoleDetail> {

	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0902Response init(Rm0902Request req) {
		req.corporationCode = CorporationCode.ASP;
		req.menuRoleCode = "ASP";
		if (isEmpty(req.corporationCode) || isEmpty(req.menuRoleCode)) {
			throw new BadRequestException("企業コードまたは利用者ロールコードが未指定です");
		}

		final Rm0902Response res = createResponse(Rm0902Response.class, req);

		// ASP管理者ロール取得
		SearchWfmMenuRoleInParam sarIn = new SearchWfmMenuRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setMenuRoleCode(req.menuRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleOutParam sarOut = wf.searchWfmMenuRole(sarIn);
		res.menuRole = sarOut.getMenuRoles().get(0);

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setMenuRoleCode(req.menuRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);
		res.menuRoleDetailList = sardOut.getMenuRoleDetails();

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.menuRole != null);
		return res;
	}

	public Rm0902Response search(Rm0902Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.menuRoleCode)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setMenuRoleCode(req.menuRoleCode);
		sardIn.setPageSize(req.pageSize);
		sardIn.setPageNo(req.pageNo);
		sardIn.setOrderBy(toOrderBy(req, ""));
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

		final List<WfmMenuRoleDetail> list = sardOut.getMenuRoleDetails();

		int allCount = sardOut.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Rm0902Response res = createResponse(Rm0902Response.class, req, allCount);

		// ASP管理者ロール取得
		SearchWfmMenuRoleInParam sarIn = new SearchWfmMenuRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setMenuRoleCode(req.menuRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleOutParam sarOut = wf.searchWfmMenuRole(sarIn);
		res.menuRole = sarOut.getMenuRoles().get(0);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = list;

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;

		return res;
	}

	/**
	 * ASP管理者ロールの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0902Response update(Rm0902UpdateRequest req) {
		// ASP管理者ロール取得
		SearchWfmMenuRoleInParam sarIn = new SearchWfmMenuRoleInParam();
		sarIn.setCorporationCode(req.menuRole.getCorporationCode());
		sarIn.setMenuRoleCode(req.menuRole.getMenuRoleCode());
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleOutParam sarOut = wf.searchWfmMenuRole(sarIn);

		if (CommonUtil.isEmpty(sarOut.getMenuRoles())) {
			throw new AlreadyUpdatedException();
		}
		WfmMenuRole menuRole = sarOut.getMenuRoles().get(0);
		menuRole.setMenuRoleName(req.menuRole.getMenuRoleName());
		menuRole.setValidStartDate(req.menuRole.getValidStartDate());
		menuRole.setValidEndDate(req.menuRole.getValidEndDate());
		menuRole.setDeleteFlag(req.menuRole.getDeleteFlag());
		menuRole.setTimestampUpdated(req.menuRole.getTimestampUpdated());

		UpdateWfmMenuRoleInParam updateIn = new UpdateWfmMenuRoleInParam();
		updateIn.setWfmMenuRole(menuRole);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmMenuRole(updateIn);

		final Rm0902Response res = createResponse(Rm0902Response.class, req);

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.menuRole));
		res.success = true;
		return res;
	}

	/**
	 * ASP管理者ロールの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0902Response delete(Rm0902Request req) {

		final Rm0902Response res = createResponse(Rm0902Response.class, req);
		// エラー
		List<String> errors = validate(req);
		if (!errors.isEmpty()) {
			res.addAlerts(errors.toArray(new String[errors.size()]));
			res.success = false;
		} else {
			for (WfmMenuRoleDetail deleteMenuRoleDetail : req.deleteMenuRoleDetails) {
				DeleteWfmMenuRoleDetailInParam deleteIn = new DeleteWfmMenuRoleDetailInParam();
				deleteIn.setWfmMenuRoleDetail(deleteMenuRoleDetail);
				deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
				wf.deleteWfmMenuRoleDetail(deleteIn);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.menuRoleDetail));

			// 削除区分の選択肢
			res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

			res.success = true;
		}
		return res;
	}

	/** バリデーション */
	private List<String> validate(Rm0902Request req) {

		final List<String> errors = new ArrayList<>();

		// ASP管理者ロール構成取得
		SearchWfmMenuRoleDetailInParam sardIn = new SearchWfmMenuRoleDetailInParam();
		sardIn.setCorporationCode(req.corporationCode);
		sardIn.setMenuRoleCode(req.menuRoleCode);
		sardIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleDetailOutParam sardOut = wf.searchWfmMenuRoleDetail(sardIn);

		// 必ず有効な1件は残す
		int delAfterSize = sardOut.getMenuRoleDetails().size() - req.deleteMenuRoleDetails.size();
		if(delAfterSize < 1) {
			errors.add(i18n.getText(MessageCd.MSG0207));
		}
		return errors;
	}
}
