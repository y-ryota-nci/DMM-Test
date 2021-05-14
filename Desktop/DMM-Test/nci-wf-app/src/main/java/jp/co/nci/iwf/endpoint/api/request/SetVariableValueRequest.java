package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Timestamp;

public class SetVariableValueRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセス定義コード. */
	private String processDefCode;
	/** プロセス定義コード枝番. */
	private String processDefDetailCode;
	/** 比較条件式変数定義コード. */
	private String variableDefCode;
	/** プロセスID. */
	private Long processId;
	/** 値. */
	private String value;
	/** 更新日時(プロセスインスタンス). */
	private Timestamp timestampUpdatedProcess;
	/** シミュレーションフラグ. */
	private boolean isSimulation;

	/**
	 * 会社コードを取得する.
	 * @return 会社コード
	 */
	public final String getCorporationCode() {
		return corporationCode;
	}
	/**
	 * 会社コードを設定する.
	 * @param pCorporationCode 会社コード
	 */
	public final void setCorporationCode(final String pCorporationCode) {
		this.corporationCode = pCorporationCode;
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
	 * @param pProcessDefCode プロセス定義コード
	 */
	public final void setProcessDefCode(final String pProcessDefCode) {
		this.processDefCode = pProcessDefCode;
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
	 * @param pProcessDefDetailCode プロセス定義コード枝番
	 */
	public final void setProcessDefDetailCode(final String pProcessDefDetailCode) {
		this.processDefDetailCode = pProcessDefDetailCode;
	}
	/**
	 * プロセス変数定義コードを取得する.
	 * @return プロセス変数定義コード
	 */
	public final String getVariableDefCode() {
		return variableDefCode;
	}
	/**
	 * 比較条件式変数定義コードを設定する.
	 * @param pVariableDefCode プロセス変数定義コード
	 */
	public final void setVariableDefCode(final String pVariableDefCode) {
		this.variableDefCode = pVariableDefCode;
	}
	/**
	 * プロセスIDを取得する.
	 * @return プロセスID
	 */
	public final Long getProcessId() {
		return processId;
	}
	/**
	 * プロセスIDを設定する.
	 * @param pProcessId プロセスID
	 */
	public final void setProcessId(final Long pProcessId) {
		this.processId = pProcessId;
	}
	/**
	 * 設定値を取得する.
	 * @return 値
	 */
	public final String getValue() {
		return value;
	}
	/**
	 * 設定値を設定する.
	 * @param pValue 値
	 */
	public final void setValue(final String pValue) {
		this.value = pValue;
	}
	/**
	 * 更新日時(プロセスインスタンス)を取得する.
	 * @return 更新日時(プロセスインスタンス)
	 */
	public final Timestamp getTimestampUpdatedProcess() {
		return this.timestampUpdatedProcess;
	}
	/**
	 * 更新日時(プロセスインスタンス)を設定する.
	 * @param pTimestampUpdatedProcess 更新日時(プロセスインスタンス)
	 */
	public final void setTimestampUpdatedProcess(final Timestamp pTimestampUpdatedProcess) {
		this.timestampUpdatedProcess = pTimestampUpdatedProcess;
	}

	/**
	 * シミュレーションフラグを取得する.
	 *
	 * @return シミュレーションフラグ
	 */
	public boolean isSimulation() {
		return isSimulation;
	}

	/**
	 * シミュレーションフラグを設定する.
	 *
	 * @param isSimulation シミュレーションフラグ
	 */
	public void setSimulation(boolean isSimulation) {
		this.isSimulation = isSimulation;
	}

}
