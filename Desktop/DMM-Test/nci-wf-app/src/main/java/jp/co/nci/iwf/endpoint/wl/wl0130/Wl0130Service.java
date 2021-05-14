package jp.co.nci.iwf.endpoint.wl.wl0130;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam.SelectMode;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.integrated_workflow.model.view.impl.WfvTrayImpl;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.tray.BaseTrayService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ワークリスト画面のサービス
 */
@BizLogic
public class Wl0130Service extends BaseTrayService {

	@Inject
	Wl0130Repository repository;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化＆初期検索
	 * @param req
	 * @return
	 */
	public Wl0130Response init(Wl0130Request req) {
		// 初期検索
		final Wl0130Response res = createResponse(Wl0130Response.class, req);

		// プロセス定義の選択肢
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final List<OptionItem> processDefs = getProcessDefOptions(corporationCode);
		res.setProcessDefs(processDefs);

		// 業務プロセス状態の選択肢
		res.setBusinessProcessStatus(lookup.getOptionItems(true, LookupTypeCode.BUSINESS_PROCESS_STATUS));

		res.userDisplayList = repository.getUserDisplayList();
		res.success = true;

		return res;
	}

	/**
	 * ワークリスト検索処理
	 * @param req
	 * @return
	 */
	public BaseResponse getWorklist(Wl0130Request req) {
		final GetActivityListInParam in = new GetActivityListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setMode(GetActivityListInParam.Mode.USER_TRAY);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setRowCount((long)req.pageSize);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);

		// ソート条件
		final List<WfSortOrder> sorts = toWfSortList(req);
		in.setSortOrderList(sorts);

		// 件数カウント
		in.setSelectMode(SelectMode.COUNT);
		final GetActivityListOutParam out1 = wf.getActivityList(in);
		final int allCount = out1.getAllCount().intValue();
		final Wl0130Response res = createResponse(Wl0130Response.class, req, allCount);

		// 結果抽出
		if (allCount == 0) {
			res.results = new ArrayList<>();
		}
		else {
			// 実データの検索
			in.setSelectMode(SelectMode.DATA);
			in.setRowNo((long)calcStartIndex(res.pageNo, req.pageSize));
			final GetActivityListOutParam out2 = wf.getActivityList(in);
			res.results = out2.getTrayList();
		}
		res.success = true;

		return res;
	}

	/**
	 * 自案件検索処理
	 * @param req
	 * @return
	 */
	public BaseResponse getOwnProcessList(Wl0130Request req) {
		final GetActivityListInParam in = new GetActivityListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setMode(GetActivityListInParam.Mode.OWN_PROCESS_TRAY);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setRowCount((long)req.pageSize);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);

		// ソート条件
		final List<WfSortOrder> sorts = toWfSortList(req);
		in.setSortOrderList(sorts);

		// 件数カウント
		in.setSelectMode(SelectMode.COUNT);
		final GetActivityListOutParam out1 = wf.getActivityList(in);
		final int allCount = out1.getAllCount().intValue();
		final Wl0130Response res = createResponse(Wl0130Response.class, req, allCount);

		// 結果抽出
		if (allCount == 0) {
			res.results = new ArrayList<>();
		}
		else {
			// 実データの検索
			in.setSelectMode(SelectMode.DATA);
			in.setRowNo((long)calcStartIndex(res.pageNo, req.pageSize));
			final GetActivityListOutParam out2 = wf.getActivityList(in);
			res.results = out2.getTrayList();
		}
		res.success = true;

		return res;
	}

	/**
	 * 汎用案件検索処理
	 * @param req
	 * @return
	 */
	public BaseResponse getAllProcessList(Wl0130Request req) {
		final GetProcessHistoryListInParam in = new GetProcessHistoryListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setMode(GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORY);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setExecuting(true);
		in.setRowCount((long)req.pageSize);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);

		// ソート条件
		final List<WfSortOrder> sorts = toWfSortList(req);
		in.setSortOrderList(sorts);

		// 件数カウント
		in.setSelectMode(SelectMode.COUNT);
		final GetProcessHistoryListOutParam out1 = wf.getProcessHistoryList(in);
		final int allCount = out1.getAllCount().intValue();
		final Wl0130Response res = createResponse(Wl0130Response.class, req, allCount);

		// 結果抽出
		if (allCount == 0) {
			res.results = new ArrayList<>();
		}
		else {
			// 実データの検索
			in.setSelectMode(SelectMode.DATA);
			in.setRowNo((long)calcStartIndex(res.pageNo, req.pageSize));
			final GetProcessHistoryListOutParam out2 = wf.getProcessHistoryList(in);

			List<WfvTray> list = new ArrayList<WfvTray>();
			out2.getTrayList().forEach(history -> {
				WfvTrayImpl tray = new WfvTrayImpl();
				try {
					BeanUtils.copyProperties(tray, history);
				} catch (IllegalAccessException iae) {
					throw new InternalServerErrorException(iae);
				} catch (InvocationTargetException ite) {
					throw new InternalServerErrorException(ite);
				}
				list.add(tray);
			}) ;
			res.results = list;
		}
		res.success = true;

		return res;
	}

	public Wl0130Response getUserDispInfo(Wl0130Request req) {
		final Wl0130Response res = createResponse(Wl0130Response.class, req);

		Long userDispId = null;
		if (!CommonUtil.isEmpty(req.userDisplayId)) {
			userDispId = new Long(req.userDisplayId);
		}
		res.userDisplayList = repository.getUserDisplayList();
		res.userDisplayConditionList = repository.getUserDisplayConditionList(req.trayType, userDispId);
		res.userDisplayColumnList = repository.getUserDisplayColumnList(req.trayType, userDispId);

		res.success = true;
		return res;
	}
}
