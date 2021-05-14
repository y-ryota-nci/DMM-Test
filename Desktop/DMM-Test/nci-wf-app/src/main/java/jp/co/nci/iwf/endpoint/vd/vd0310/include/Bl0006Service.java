package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetActionHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActionHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSortOrderImpl;
import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.HistoryInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：履歴情報のサービス
 */
@BizLogic
public class Bl0006Service extends BaseService implements CodeMaster {

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	public List<HistoryInfo> getHistoryList(String corporationCode, Long processId) {
		GetActionHistoryListInParam in = new GetActionHistoryListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());

		in.setSortType(CommonFlag.OFF);
		List<WfSortOrder> sortOrderList = new ArrayList<WfSortOrder>();
		WfSortOrder wfSortOrder = new WfSortOrderImpl();
		wfSortOrder.setSortAsNumber(false);
		wfSortOrder.setSortOrderType(SortOrderType.ASC);
		wfSortOrder.setColumnName(WfvActionHistory.EXECUTION_DATE);
		sortOrderList.add(wfSortOrder);
		in.setSortOrderList(sortOrderList);

		in.setProcessId(processId);
		in.setExecuting(true);
		in.setMode(GetActionHistoryListInParam.Mode.PROCESS_A_HISTORY);
		in.setSelectMode(GetActionHistoryListInParam.SelectMode.DATA);	// 実データのみ

		// 実行
		GetActionHistoryListOutParam out = wf.getActionHistoryList(in);
		List<WfvActionHistory> actionHistoryList = out.getTrayList();
		List<HistoryInfo> results = new ArrayList<>();

		for (WfvActionHistory h : actionHistoryList) {
			String activityStatus = h.getActivityStatus();
			String assignedStatus = h.getAssignedStatus();
			Timestamp executionDate = h.getExecutionDate();

			if (isNotEmpty(activityStatus) &&
					(eq(ActivityStatus.END, activityStatus)
					|| eq(ActivityStatus.END_P, activityStatus)
					|| eq(ActivityStatus.END_S, activityStatus)
					|| eq(ActivityStatus.END_C, activityStatus)
					|| eq(ActivityStatus.F_END, activityStatus)
					|| eq(ActivityStatus.F_END_S, activityStatus)
					|| eq(ActivityStatus.F_END_C, activityStatus)
					|| eq(ActivityStatus.F_ABORT, activityStatus)
					|| eq(ActivityStatus.A_SKIP, activityStatus)
					|| (eq(ActivityStatus.ABORT, activityStatus) && eq(AssignedStatus.END, assignedStatus))
					|| (eq(ActivityStatus.ABORT, activityStatus) && eq(AssignedStatus.END_C, assignedStatus))
					|| (eq(ActivityStatus.ABORT, activityStatus) && eq(AssignedStatus.END_P, assignedStatus))
					|| (eq(ActivityStatus.ABORT, activityStatus) && eq(AssignedStatus.END_S, assignedStatus))
					|| (eq(ActivityStatus.START, activityStatus) && executionDate != null)
					|| (eq(ActivityStatus.RUN, activityStatus) && executionDate != null))
			) {
				HistoryInfo e = new HistoryInfo(h);
				results.add(e);
			}
		}
		return results;
	}
}
