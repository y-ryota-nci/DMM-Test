package jp.co.nci.iwf.endpoint.api.response;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WftActivity;
import jp.co.nci.integrated_workflow.model.base.WftAssigned;
import jp.co.nci.integrated_workflow.model.base.WftProcess;

public class StopProcessInstanceResponse extends ApiBaseResponse {
	/** プロセスインスタンス. */
	private WftProcess process;
	/** アクティビティインスタンスのリスト(業務フロー連携用). */
	private List<WftActivity> activityList;
	/** 参加者インスタンスのリスト(業務フロー連携用). */
	private List<WftAssigned> assignedList;
	/** 前プロセスインスタンス(業務フロー連携用). */
	private WftProcess processPrev;
	/** 中断された参加者インスタンスのリスト. */
	private List<WftAssigned> abortAssignedList;

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
	 * 中断された参加者インスタンスのリストを取得する.
	 * @return 中断された参加者インスタンスのリスト
	 */
	public final List<WftAssigned> getAbortAssignedList() {
		return abortAssignedList;
	}
	/**
	 * 中断された参加者インスタンスのリストを設定する.
	 * @param abortAssignedList 中断された参加者インスタンスのリスト
	 */
	public final void setAbortAssignedList(final List<WftAssigned> abortAssignedList) {
		this.abortAssignedList = abortAssignedList;
	}
}
