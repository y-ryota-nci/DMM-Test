package jp.co.nci.iwf.endpoint.cm.cm0060;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam.SelectMode;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.integrated_workflow.model.view.impl.WfvTrayImpl;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.tray.BaseTrayService;

/**
 * 関連文書選択画面Service
 */
@BizLogic
public class Cm0060Service extends BaseTrayService {
	@Inject private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0060InitResponse init(Cm0060InitRequest req) {
		final Cm0060InitResponse res = createResponse(Cm0060InitResponse.class, req);

		// プロセス定義の選択肢
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final List<OptionItem> processDefs = getProcessDefOptions(corporationCode);
		res.setProcessDefs(processDefs);

		// 業務プロセス状態の選択肢
		res.setBusinessProcessStatus(
				lookup.getOptionItems(true, LookupTypeCode.BUSINESS_PROCESS_STATUS));

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0060SearchResponse search(Cm0060SearchRequest req) {
		final GetProcessHistoryListInParam in = new GetProcessHistoryListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setMode(null);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setRowCount((long)req.pageSize);

		// 実行中のインスタンスも取得する
		in.setExecuting(true);
		// 過去案件とマージするかをセット
		in.setMerge(false);
		in.setMode(GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORY);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);
		if (!StringUtils.isEmpty(req.notInProcessIds)) {
			WfSearchCondition<Object> sc = new WfSearchConditionImpl<Object>();
			sc.setColumnName(WftProcess.PROCESS_ID);
			sc.setSearchCondtionType(SearchConditionType.NOT_IN);
			List<String> conditionValue = Arrays.asList(req.notInProcessIds.split(","));
			sc.setSearchConditionValue(conditionValue);
			in.getSearchConditionList().add(sc);
		}
		// ソート条件
		final List<WfSortOrder> sorts = toWfSortList(req);
		in.setSortOrderList(sorts);

		// 件数カウント
		in.setSelectMode(SelectMode.COUNT);
		final GetProcessHistoryListOutParam out1 = wf.getProcessHistoryList(in);
		final int allCount = out1.getAllCount().intValue();
		final Cm0060SearchResponse res = createResponse(Cm0060SearchResponse.class, req, allCount);

		// 結果抽出
		if (allCount == 0) {
			res.results = new ArrayList<>();
		}
		else {
			// 実データの検索
			in.setSelectMode(SelectMode.DATA);
			in.setRowNo((long)calcStartRowNo(res.pageNo, req.pageSize));
			final GetProcessHistoryListOutParam out2 = wf.getProcessHistoryList(in);
			final List<WfvTray> list = new ArrayList<WfvTray>();
			out2.getTrayList().forEach(history -> {
				final WfvTray tray = new WfvTrayImpl();
				copyProperties(history, tray);
				list.add(tray);
			}) ;
			res.results = list;
		}
		res.success = true;
		return res;
	}
}
