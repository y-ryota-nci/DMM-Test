package jp.co.nci.iwf.component.tray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetLatestActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessableActivityListInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.SortOrderType;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfLatestHistory;
import jp.co.nci.integrated_workflow.model.custom.WfProcessableActivity;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.impl.WfProcessableActivityImpl;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSortOrderImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0006Service;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * ワークリストなどトレイ関連の画面用サービスの基底クラス
 */
public abstract class BaseTrayService extends BasePagingService implements SearchConditionType {
	/** WF API */
	@Inject protected WfInstanceWrapper wf;
	/** 承認履歴情報ブロックサービス */
	@Inject protected Bl0006Service bl0006;
	/** WFM_LOOKUPサービス */
	@Inject protected WfmLookupService wfmLookup;

	/** プロセス定義の選択肢 */
	protected List<OptionItem> getProcessDefOptions(String corporationCode) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[]{
				new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
		});
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 仮に有効期限が切れたとしても、過去案件は検索対象に含まれなければならない。
		// よってプロセス定義に対して有効期限で絞り込むのは誤りである。

		final List<OptionItem> optionItems = new ArrayList<>();
		optionItems.add(OptionItem.EMPTY);

		// 枝番があるのでプロセス定義コードは重複する可能性があるので、ユニーク化する
		final Set<String> uniques = new HashSet<>();
		wf.searchWfmProcessDef(in).getProcessDefs().forEach(e -> {
			if (!uniques.contains(e.getProcessDefCode())) {
				optionItems.add(new OptionItem(
						e.getProcessDefCode(),
						e.getProcessDefName()
				));
				uniques.add(e.getProcessDefCode());
			}
		});
		return optionItems;
	}

	/**
	 * ソート条件リストを生成
	 * @param req
	 * @return
	 */
	protected List<WfSortOrder> toWfSortList(BasePagingRequest req) {
		final List<WfSortOrder> sorts = new ArrayList<>();
		if (isNotEmpty(req.sortColumn)) {
			String[] sortColumns = req.sortColumn.split(",\\s*");
			for (String col : sortColumns){
				sorts.add(toWfSort(col, req.sortAsc));
			}
		}
		return sorts;
	}

	/**
	 * WF APIのソート型へ変換
	 * @param colName ソートするカラム名(エイリアスがあればそれも含む）
	 * @param asc 昇順ならtrue、降順ならfalse
	 * @return
	 */
	protected WfSortOrder toWfSort(String colName, boolean asc) {
		final WfSortOrder sort = new WfSortOrderImpl();
		sort.setColumnName(colName);
		sort.setSortAsNumber(false);
		sort.setSortOrderType(asc ? SortOrderType.ASC : SortOrderType.DESC);
		return sort;
	}

	/**
	 * 絞り込み条件を生成
	 * @param req
	 * @return
	 */
	protected List<WfSearchCondition<?>> toWfSearchCondition(BaseTrayRequest req) {
		List<WfSearchCondition<?>> conds = new ArrayList<>();
		{
			// プロセス定義コード
			if (isNotEmpty(req.processDefCode)) {
				final String[] astr = req.processDefCode.split("_");
				if (astr.length > 0)
					conds.add(toWfCond(WfvTray.PROCESS_DEF_CODE, EQUAL, astr[0]));
				if (astr.length > 1)
					conds.add(toWfCond(WfvTray.PROCESS_DEF_DETAIL_CODE, EQUAL, astr[1]));
			}
			// 件名
			if (isNotEmpty(req.subject)) {
				conds.add(toWfCond(WfvTray.SUBJECT, MATCH, req.subject));
			}
			// 申請番号
			if (isNotEmpty(req.applicationNo)) {
				conds.add(toWfCond(WfvTray.APPLICATION_NO, MATCH, req.applicationNo));
			}
			// 決裁番号
			if (isNotEmpty(req.approvalNo)) {
				conds.add(toWfCond(WfvTray.APPROVAL_NO, MATCH, req.approvalNo));
			}
			// 下限金額
			if (isNotEmpty(req.amountMin)) {
				conds.add(toWfCond(WfvTray.AMOUNT, GRATER_EQUAL, req.amountMin));
			}
			// 上限金額
			if (isNotEmpty(req.amountMax)) {
				conds.add(toWfCond(WfvTray.AMOUNT, LESS_EQUAL, req.amountMax));
			}
			// 業務プロセス状態
			if (isNotEmpty(req.businessProcessStatus)) {
				conds.add(toWfCond(WfvTray.BUSINESS_PROCESS_STATUS, EQUAL, req.businessProcessStatus));
			}
		}
		return conds;
	}

	/**
	 * WF APIの検索条件型へ変換
	 * @param colName カラム名
	 * @param op '=', 'like' などの演算子。SearchConditionTypeの定数を参照のこと
	 * @param val 値
	 * @return
	 */
	protected <V> WfSearchCondition<V> toWfCond(String colName, String op, V val) {
		final WfSearchCondition<V> cond = new WfSearchConditionImpl<>();
		cond.setColumnName(colName);
		cond.setSearchCondtionType(op);
		cond.setSearchConditionValue(val);
		return cond;
	}

	/**
	 * トレイタイプごとに、操作者がプロセス情報にアクセス可能な最新の履歴情報を返す。
	 * 【前提】事前に操作者がそのプロセス情報にアクセス可能であることを確認済みであること。
	 * @param corporationCode 企業コード
	 * @param processId プロセスID
	 * @param trayType トレイタイプ
	 * @return
	 */
	public List<WfProcessableActivity> getAccessibleActivity(String corporationCode, long processId, TrayType trayType) {
		if (eq(TrayType.FORCE, trayType)) {
			// 強制変更は常に最新のアクティビティ
			return getLastestHistory(corporationCode, processId);
		}
		else if (eq(TrayType.ALL, trayType)) {
			// 操作者のアクセス可能なアクティビティ一覧
			final GetProcessableActivityListInParam in = new GetProcessableActivityListInParam();
			in.setCorporationCode(corporationCode);
			in.setProcessId(processId);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			return wf.getProcessableActivityList(in).getWfProcessableActivityList();
		}
		else {
			throw new IllegalAccessError("TrayType.ALL or FORCE以外ではトレイ系APIでActivityが一意に定まっているため、このメソッドからActivityを求めるのは誤った実装です。");
		}
	}

	/** 最新の履歴情報を抽出 */
	private List<WfProcessableActivity> getLastestHistory(String corporationCode, Long processId) {
		// 最新の履歴情報を抽出
		// ここでいう最新とは、差戻し等により同一ユーザが同じアクティビティを処理することがあるので、それを除外した履歴のこと
		// 履歴なので、自分以外が承認した内容も含んでいることに留意せよ
		final GetLatestActivityListInParam in = new GetLatestActivityListInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		in.setMode(GetLatestActivityListInParam.Mode.ACTIVITY);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		List<WfLatestHistory> srcList = wf.getLatestActivityList(in).getWfLatestHistoryList();

		// 逆順にソート
		List<WfProcessableActivity> list = new ArrayList<>(srcList.size());
		for (int i = srcList.size() - 1; 0 <= i; i--) {
			list.add(new WfProcessableActivityImpl(srcList.get(i)));
		}
		return list;
	}
}
