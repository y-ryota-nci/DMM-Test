package jp.co.nci.iwf.endpoint.api.response;

import java.math.BigDecimal;
import java.util.List;

import jp.co.nci.integrated_workflow.model.view.WfvTray;

public class GetActivityListResponse extends ApiBaseResponse {

	/** トレイ情報のリスト. */
	private List<WfvTray> wfvTrayList;
	/** トレイ情報の全件数. */
	private BigDecimal allCount;

	/**
	 * トレイ情報のリストを取得する.
	 * @return wfvTrayList
	 */
	public final List<WfvTray> getWfvTrayList() {
		return wfvTrayList;
	}

	/**
	 * トレイ情報のリストを設定する.
	 * @param val 設定するトレイ情報のリスト
	 */
	public final void setWfvTrayList(final List<WfvTray> val) {
		this.wfvTrayList = val;
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
