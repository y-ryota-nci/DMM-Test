package jp.co.nci.iwf.endpoint.rm.rm0020;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmMenuRoleInParam;
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
 * 利用者ロール設定サービス
 */
@BizLogic
public class Rm0020Service extends MmBaseService<WfmMenuRoleDetail> {

	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0020Response init(Rm0020Request req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.menuRoleCode))
			throw new BadRequestException("企業コードまたは利用者ロールコードが未指定です");

		final Rm0020Response res = createResponse(Rm0020Response.class, req);

		// 利用者ロール取得
		SearchWfmMenuRoleInParam sarIn = new SearchWfmMenuRoleInParam();
		sarIn.setCorporationCode(req.corporationCode);
		sarIn.setMenuRoleCode(req.menuRoleCode);
		sarIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleOutParam sarOut = wf.searchWfmMenuRole(sarIn);
		res.menuRole = sarOut.getMenuRoles().get(0);

		// 利用者ロール構成取得
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

	public Rm0020Response search(Rm0020Request req) {
		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.menuRoleCode)) {
			throw new BadRequestException("ルックアップグループIDが未指定です");
		}

		// 利用者ロール構成取得
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
		final Rm0020Response res = createResponse(Rm0020Response.class, req, allCount);

		// 利用者ロール取得
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
	 * ルックアップグループの更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0020Response update(Rm0020UpdateRequest req) {
		// 利用者ロール取得
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

		final Rm0020Response res = createResponse(Rm0020Response.class, req);

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.menuRole));
		res.success = true;
		return res;
	}

	/**
	 * ルックアップの削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0020Response delete(Rm0020Request req) {
		for (WfmMenuRoleDetail deleteMenuRoleDetail : req.deleteMenuRoleDetails) {
			DeleteWfmMenuRoleDetailInParam deleteIn = new DeleteWfmMenuRoleDetailInParam();
			deleteIn.setWfmMenuRoleDetail(deleteMenuRoleDetail);
			deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
			wf.deleteWfmMenuRoleDetail(deleteIn);
		}
		final Rm0020Response res = createResponse(Rm0020Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0063, MessageCd.menuRoleDetail));

		// 削除区分の選択肢
		res.deleteFlagList = lookup.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

}
