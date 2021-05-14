package jp.co.nci.iwf.endpoint.na.na0001;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * メニューロール一覧のサービス
 */
@BizLogic
public class Na0001Service extends BasePagingService implements CodeMaster {

	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Na0001Response init(Na0001Request req) {
		final Na0001Response res = createResponse(Na0001Response.class, req);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Na0001Response search(Na0001Request req) {
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
		final Na0001Response res = createResponse(Na0001Response.class, req, outParam.getCount());
		res.results = outParam.getMenuRoles();
		res.success = true;
		return res;
	}

}