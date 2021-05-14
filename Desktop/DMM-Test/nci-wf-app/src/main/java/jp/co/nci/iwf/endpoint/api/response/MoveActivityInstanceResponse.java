package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.model.base.WftActivity;
import jp.co.nci.integrated_workflow.model.base.WftAssigned;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfSkipAction;

public class MoveActivityInstanceResponse extends ApiBaseResponse {

	/** プロセスインスタンス. */
	private WftProcess process;
	/** 遷移後のアクティビティインスタンスのリスト. */
	private List<WftActivity> activityList;
	/** 遷移後の参加者インスタンスのリスト. */
	private List<WftAssigned> assignedList;
	/** 遷移元のアクティビティインスタンスのリスト. */
	private List<WftActivity> activityPrevList;
	/** 遷移元の参加者インスタンスのリスト. */
	private List<WftAssigned> assignedPrevList;
	/** 遷移後の業務プロセス状態. */
	private String businessProcessStatus;
	/** 遷移後の業務アクティビティ状態. */
	private String businessActivtyStatus;
	/** WF連携で起票されたプロセス. */
	private List<CreateProcessInstanceOutParam> nextProcessInstanceList;
	/** 前プロセスインスタンス. */
	private WftProcess processPrev;
	/** スキップ処理対象一覧. */
	private List<WfSkipAction> skipActionList;

	/**
	 * プロセスインスタンスを取得する.
	 * @return プロセスインスタンス
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
	 * 遷移後のアクティビティインスタンスのリストを取得する.
	 * @return 遷移後のアクティビティインスタンスのリスト
	 */
	public final List<WftActivity> getActivityList() {
		return activityList;
	}
	/**
	 * 遷移後のアクティビティインスタンスのリストを設定する.
	 * @param pActivityList 設定する遷移後のアクティビティインスタンスのリスト
	 */
	public final void setActivityList(final List<WftActivity> pActivityList) {
		this.activityList = pActivityList;
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
	 * 遷移元のアクティビティインスタンスのリストを取得する.
	 * @return 遷移元のアクティビティインスタンスのリスト
	 */
	public final List<WftActivity> getActivityPrevList() {
		return activityPrevList;
	}
	/**
	 * 遷移元のアクティビティインスタンスのリストを設定する.
	 * @param pAssignedPrevList 設定する遷移元のアクティビティインスタンスのリスト
	 */
	public final void setActivityPrevList(List<WftActivity> activityPrevList) {
		this.activityPrevList = activityPrevList;
	}
	/**
	 * 遷移元の参加者インスタンスのリストを取得する.
	 * @return 遷移元の参加者インスタンスのリスト
	 */
	public final List<WftAssigned> getAssignedPrevList() {
		return assignedPrevList;
	}
	/**
	 * 遷移元の参加者インスタンスのリストを設定する.
	 * @param pAssignedPrevList 設定する遷移元の参加者インスタンスのリスト
	 */
	public final void setAssignedPrevList(final List<WftAssigned> pAssignedPrevList) {
		this.assignedPrevList = pAssignedPrevList;
	}
	/**
	 * 遷移後の業務アクティビティ状態を取得する.
	 * @return businessActivtyStatus
	 */
	public final String getBusinessActivtyStatus() {
		return businessActivtyStatus;
	}
	/**
	 * 遷移後の業務アクティビティ状態を設定する.
	 * @param pBusinessActivtyStatus 設定する遷移後の業務アクティビティ状態
	 */
	public final void setBusinessActivtyStatus(final String pBusinessActivtyStatus) {
		this.businessActivtyStatus = pBusinessActivtyStatus;
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
	 * WF連携で起票されたプロセスを取得する.
	 * @return WF連携で起票されたプロセス
	 */
	public final List<CreateProcessInstanceOutParam> getNextProcessInstanceList() {
		return nextProcessInstanceList;
	}
	/**
	 * WF連携で起票されたプロセスを設定する.
	 * @param pNextProcessInstanceList WF連携で起票されたプロセス
	 */
	public final void setNextProcessInstanceList(
			final List<CreateProcessInstanceOutParam> pNextProcessInstanceList) {
		this.nextProcessInstanceList = pNextProcessInstanceList;
	}
	/**
	 * 前プロセスインスタンスを取得する.
	 * @return processPrev 前プロセスインスタンス
	 */
	public final WftProcess getProcessPrev() {
		return processPrev;
	}
	/**
	 * 前プロセスインスタンスを設定する.
	 * @param pProcessPrev 前プロセスインスタンス
	 */
	public final void setProcessPrev(final WftProcess pProcessPrev) {
		this.processPrev = pProcessPrev;
	}
	/**
	 * スキップ処理対象一覧を取得する.
	 *
	 * @return スキップ処理対象一覧
	 */
	public List<WfSkipAction> getSkipActionList() {
		return skipActionList;
	}
	/**
	 * スキップ処理対象一覧を設定する.
	 *
	 * @param skipActionList スキップ処理対象一覧
	 */
	public void setSkipActionList(List<WfSkipAction> skipActionList) {
		this.skipActionList = skipActionList;
	}
}
