package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfvRoute;

public class GetRouteDefResponse extends ApiBaseResponse {

	/** 承認ルートのリスト. */
	private List<WfvRoute> wfvRouteList;

	/**
	 * 承認ルートのリストを取得する.
	 * @return wfvRoutList
	 */
	public final List<WfvRoute> getWfvRouteList() {
		return wfvRouteList;
	}

	/**
	 * 承認ルートのリストを設定する.
	 * @param val 設定する承認ルート定義情報のリスト
	 */
	public final void setWfvRouteList(final List<WfvRoute> val) {
		this.wfvRouteList = val;
	}

}
