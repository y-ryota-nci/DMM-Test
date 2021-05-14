package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.model.base.WftActivity;
import jp.co.nci.integrated_workflow.model.base.WftAssigned;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.base.WftVariable;

public class CreateProcessInstanceResponse extends ApiBaseResponse {

	/** プロセスインスタンス. */
	private WftProcess process;
	/** アクティビティインスタンス. */
	private WftActivity activity;
	/** 遷移後の参加者インスタンスのリスト. */
	private List<WftAssigned> assignedList;
	/** プロセス変数インスタンスのリスト. */
	private List<WftVariable> variableList;
	/** 遷移後の業務プロセス状態. */
	private String businessProcessStatus;
	/** スキップ処理結果. */
	private MoveActivityInstanceOutParam skipActivityInstance;

	/**
	 * アクティビティインスタンスを取得する.
	 * @return アクティビティインスタンス
	 */
	public final WftActivity getActivity() {
		return activity;
	}
	/**
	 * アクティビティインスタンスを設定する.
	 * @param pActivity 設定するアクティビティインスタンス
	 */
	public final void setActivity(final WftActivity pActivity) {
		this.activity = pActivity;
	}
	/**
	 * プロセスインスタンスを取得する.
	 * @return process
	 */
	public final WftProcess getProcess() {
		return process;
	}
	/**
	 * プロセスインスタンスを設定する.
	 * @param pProcess 設定するプロセスインスタンス
	 */
	public final void setProcess(final WftProcess pProcess) {
		this.process = pProcess;
	}
	/**
	 * 遷移後の業務プロセス状態を取得する.
	 * @return 遷移後の業務プロセス状態

	 */
	public final String getBusinessProcessStatus() {
		return businessProcessStatus;
	}
	/**
	 * 遷移後の業務プロセス状態を設定する.
	 * @param pBusinessProcessStatus 設定する遷移後の業務プロセス状態

	 */
	public final void setBusinessProcessStatus(final String pBusinessProcessStatus) {
		this.businessProcessStatus = pBusinessProcessStatus;
	}
	/**
	 * スキップ処理結果を取得する.
	 * @return スキップ処理結果
	 */
	public final MoveActivityInstanceOutParam getSkipActivityInstance() {
		return skipActivityInstance;
	}
	/**
	 * スキップ処理結果を設定する.
	 * @param pSkipActivityInstance スキップ処理結果
	 */
	public final void setSkipActivityInstance(
			final MoveActivityInstanceOutParam pSkipActivityInstance) {
		this.skipActivityInstance = pSkipActivityInstance;
	}
	/**
	 * 遷移後の参加者インスタンスのリストを取得する.
	 * @return 遷移後の参加者インスタンスのリスト

	 */
	public final List<WftAssigned> getAssignedList() {
		return assignedList;
	}
	/**
	 * 遷移後の参加者インスタンスのリストを設定する.
	 * @param pAssignedList 設定する遷移後の参加者インスタンスのリスト

	 */
	public final void setAssignedList(final List<WftAssigned> pAssignedList) {
		this.assignedList = pAssignedList;
	}
	/**
	 * プロセス変数インスタンスのリストを取得する.
	 * @return プロセス変数インスタンスのリスト

	 */
	public final List<WftVariable> getVariableList() {
		return variableList;
	}
	/**
	 * プロセス変数インスタンスのリストを設定する.
	 * @param pVariableList プロセス変数インスタンスのリスト

	 */
	public final void setVariableList(final List<WftVariable> pVariableList) {
		this.variableList = pVariableList;
	}

}
