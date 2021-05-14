package jp.co.nci.iwf.endpoint.rm.rm0100;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 参加者ロール一覧のサービス
 */
@BizLogic
public class Rm0100Service extends MmBaseService<WfmMenuRole> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0100Response init(Rm0100Request req) {
		final Rm0100Response res = createResponse(Rm0100Response.class, req);
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
	public Rm0100Response search(Rm0100Request req) {
		SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setMenuRoleCode(req.menuRoleCode);
		inParam.setMenuRoleName(req.menuRoleName);
		inParam.setMenuRoleType(MenuRoleType.NORMAL);
		inParam.setValidStartDate(req.validStartDate);
		inParam.setValidEndDate(req.validEndDate);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setPageSize(req.pageSize);
		inParam.setPageNo(req.pageNo);
		inParam.setOrderBy(toOrderBy(req, "MR."));
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(inParam);
		final List<WfmMenuRole> list = outParam.getMenuRoles();

		int allCount = outParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Rm0100Response res = createResponse(Rm0100Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = list;
		res.success = true;
		return res;
	}
}
