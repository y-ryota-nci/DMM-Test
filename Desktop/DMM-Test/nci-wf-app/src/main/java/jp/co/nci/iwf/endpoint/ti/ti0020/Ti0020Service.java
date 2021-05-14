package jp.co.nci.iwf.endpoint.ti.ti0020;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メニューロール一覧（マスタ権限設定）サービス
 */
@BizLogic
public class Ti0020Service extends BasePagingService {
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0020Response init(BaseRequest req) {
		Ti0020Response res = createResponse(Ti0020Response.class, req);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ti0020Response search(Ti0020Request req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		SearchWfmMenuRoleInParam in = new SearchWfmMenuRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setMenuRoleCode(req.menuRoleCode);
		in.setMenuRoleName(req.menuRoleName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setDeleteFlag(req.deleteFlag);
		in.setMenuRoleType(MenuRoleType.NORMAL);
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);

		final List<OrderBy> sorts = new ArrayList<>();
		if (isNotEmpty(req.sortColumn)) {
			String[] sortColumns = req.sortColumn.split(",\\s*");
			for (String col : sortColumns){
				sorts.add(new OrderBy(sorts.isEmpty() && !req.sortAsc ? OrderBy.DESC : OrderBy.ASC, col));
			}
		}
		in.setOrderBy(sorts.toArray(new OrderBy[]{}));

		// 検索
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Ti0020Response res = createResponse(Ti0020Response.class, req, outParam.getCount());
		res.results = outParam.getMenuRoles();
		res.success = true;
		return res;
	}
}
