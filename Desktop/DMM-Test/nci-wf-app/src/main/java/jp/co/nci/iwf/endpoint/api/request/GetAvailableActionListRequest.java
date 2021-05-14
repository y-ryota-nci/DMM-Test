package jp.co.nci.iwf.endpoint.api.request;

import java.util.List;

public class GetAvailableActionListRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセス定義コード. */
	private String processDefCode;
	/** プロセス定義詳細コード. */
	private String processDefDetailCode;
	/** アクティビティ定義コード. */
	private String activityDefCode;
	/** 除外するアクションコード. */
	private List<String> actionCodeOfExclusionList;

	/**
	 * 除外アクションコードのリストを取得する.
	 * @return 除外するアクションコードのリスト
	 */
	public final List<String> getActionCodeOfExclusionList() {
		return actionCodeOfExclusionList;
	}
	/**
	 * 除外アクションコードのリストを設定する.
	 * @param val 設定する除外アクションコードのリスト
	 */
	public final void setActionCodeOfExclusion(final List<String> val) {
		this.actionCodeOfExclusionList = val;
	}
	/**
	 * アクティビティ定義コードを取得する.
	 * @return アクティビティ定義コード
	 */
	public final String getActivityDefCode() {
		return activityDefCode;
	}
	/**
	 * アクティビティ定義コードを設定する.
	 * @param val 設定するアクティビティ定義コード
	 */
	public final void setActivityDefCode(final String val) {
		this.activityDefCode = val;
	}
	/**
	 * プロセス定義コードを取得する.
	 * @return プロセス定義コード
	 */
	public final String getProcessDefCode() {
		return processDefCode;
	}
	/**
	 * プロセス定義コードを設定する.
	 * @param val 設定するプロセス定義コード
	 */
	public final void setProcessDefCode(final String val) {
		this.processDefCode = val;
	}
	/**
	 * プロセス定義コード枝番を取得する.
	 * @return プロセス定義コード枝番
	 */
	public final String getProcessDefDetailCode() {
		return processDefDetailCode;
	}
	/**
	 * プロセス定義コード枝番を設定する.
	 * @param val 設定するプロセス定義コード枝番
	 */
	public final void setProcessDefDetailCode(final String val) {
		this.processDefDetailCode = val;
	}
	/**
	 * 会社コードを取得する.
	 * @return 会社コード
	 */
	public final String getCorporationCode() {
		return corporationCode;
	}
	/**
	 * 会社コードを設定する.
	 * @param pCorporationCode 設定する会社コード
	 */
	public final void setCorporationCode(final String pCorporationCode) {
		this.corporationCode = pCorporationCode;
	}

}
