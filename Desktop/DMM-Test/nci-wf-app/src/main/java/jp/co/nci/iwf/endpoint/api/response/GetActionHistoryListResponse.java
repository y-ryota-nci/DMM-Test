package jp.co.nci.iwf.endpoint.api.response;

import java.math.BigDecimal;
import java.util.List;

import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;

public class GetActionHistoryListResponse extends ApiBaseResponse {

	/** 承認履歴のリスト. */
	private List<WfvActionHistory> wfvActionHistoryList;
	/** トレイ情報の全件数. */
	private BigDecimal allCount;

	/**
	 * 承認履歴のリストを取得する.
	 * @return 承認履歴のリスト
	 */
	public final List<WfvActionHistory> getWfvActionHistoryList() {
		return wfvActionHistoryList;
	}

	/**
	 * 承認履歴のリストを設定する.
	 * @param val 設定する承認履歴のリスト
	 */
	public final void setWfvActionHistoryList(final List<WfvActionHistory> val) {
		this.wfvActionHistoryList = val;
	}

	/**
	 * 全件数を取得する.
	 * @return 全件数
	 */
	public final BigDecimal getAllCount() {
		return allCount;
	}

	/**
	 * 全件数を設定する.
	 * @param pAllCount 設定する全件数
	 */
	public final void setAllCount(final BigDecimal pAllCount) {
		this.allCount = pAllCount;
	}

}
