package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmActivityDef;

/**
 * 引戻先アクティビティ定義照会API戻り値格納クラス.
 */
public class GetPullbackActivityDefListResponse extends ApiBaseResponse {

	/** アクティビティ定義のリスト. */
	private List<WfmActivityDef> wfmActivityDefList;

	/**
	 * アクティビティ定義のリストを取得する.
	 * @return アクティビティ定義のリスト
	 */
	public final List<WfmActivityDef> getWfmActivityDefList() {
		return wfmActivityDefList;
	}

	/**
	 * アクティビティ定義のリストを設定する.
	 * @param val 設定するアクティビティ定義のリスト
	 */
	public final void setWfmActivityDefList(final List<WfmActivityDef> val) {
		this.wfmActivityDefList = val;
	}

}
