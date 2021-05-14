package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.model.view.WfvActionDef;

public class GetAvailableActionListResponse extends ApiBaseResponse {

	/** アクション定義のリスト. */
	private List<WfvActionDef> wfvActionDefList;

	/**
	 * アクション定義のリストを取得する.
	 * @return アクション定義のリスト
	 */
	public final List<WfvActionDef> getWfvActionDefList() {
		return wfvActionDefList;
	}

	/**
	 * アクション定義のリストを設定する.
	 * @param val 設定するアクション定義のリスト
	 */
	public final void setWfvActionDefList(final List<WfvActionDef> val) {
		this.wfvActionDefList = val;
	}

}
