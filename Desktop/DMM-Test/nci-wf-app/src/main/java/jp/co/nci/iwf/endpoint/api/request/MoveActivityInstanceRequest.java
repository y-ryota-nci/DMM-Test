package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Timestamp;
import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;

public class MoveActivityInstanceRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセスID. */
	private Long processId;
	/** アクティビティID. */
	private Long activityId;
	/** アクションコード. */
	private String actionCode;
	/** 遷移先ユーザロール. */
	private WfUserRole wfUserRoleTransfer;
	/** 遷移先会社コード. */
	private String corporationCodeTransfer;
	/** 遷移先プロセス定義コード. */
	private String processDefCodeTransfer;
	/** 遷移先プロセス定義コード枝番. */
	private String processDefDetailCodeTransfer;
	/** スキップ実行指示. */
	private boolean isSkip;
	/** 業務管理情報01. */
	private String businessInfo01;
	/** 業務管理情報02. */
	private String businessInfo02;
	/** 業務管理情報03. */
	private String businessInfo03;
	/** 業務管理情報04. */
	private String businessInfo04;
	/** 業務管理情報05. */
	private String businessInfo05;
	/** 業務管理情報06. */
	private String businessInfo06;
	/** 業務管理情報07. */
	private String businessInfo07;
	/** 業務管理情報08. */
	private String businessInfo08;
	/** 業務管理情報09. */
	private String businessInfo09;
	/** 業務管理情報10. */
	private String businessInfo10;
	/** 更新日時(プロセスインスタンス). */
	private Timestamp timestampUpdatedProcess;
	/** 連携先プロセス定義リスト. */
	private List<WfmProcessDef> nextWfmProcessDefList;

	/** (新)起票組織コード. */
	private String newOrganizationCodeStart;
	/** (新)起票役職コード. */
	private String newPostCodeStart;
	/** (新)起票者ユーザロール. */
	private WfUserRole newWfUserRoleStart;

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
	 * アクションコードを取得する.
	 * @return アクションコード
	 */
	public final String getActionCode() {
		return actionCode;
	}

	/**
	 * アクションコードを設定する.
	 * @param val 設定するアクションコード
	 */
	public final void setActionCode(final String val) {
		this.actionCode = val;
	}

	/**
	 * アクティビティIDを取得する.
	 * @return アクティビティID
	 */
	public final Long getActivityId() {
		return activityId;
	}

	/**
	 * アクティビティIDを設定する.
	 * @param val 設定するアクティビティID
	 */
	public final void setActivityId(final Long val) {
		this.activityId = val;
	}

	/**
	 * スキップ実行指示を判定する.
	 * @return スキップ実行支持
	 */
	public final boolean isSkip() {
		return isSkip;
	}

	/**
	 * スキップ実行指示を設定する.
	 * @param val 設定するスキップ実行支持
	 */
	public final void setSkip(final boolean val) {
		this.isSkip = val;
	}

	/**
	 * 遷移先プロセス定義コードを取得する.
	 * @return 遷移先プロセス定義コード
	 */
	public final String getProcessDefCodeTransfer() {
		return processDefCodeTransfer;
	}

	/**
	 * 遷移先プロセス定義コードを設定する.
	 * @param val 設定する遷移先プロセス定義コード
	 */
	public final void setProcessDefCodeTransfer(final String val) {
		this.processDefCodeTransfer = val;
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
	 * @param val 設定するプロセスID
	 */
	public final void setProcessId(final Long val) {
		this.processId = val;
	}

	/**
	 * 遷移先ユーザロールを取得する.
	 * @return 遷移先ユーザロール
	 */
	public final WfUserRole getWfUserRoleTransfer() {
		return wfUserRoleTransfer;
	}

	/**
	 * 遷移先ユーザロールを設定する.
	 * @param val 設定する遷移先ユーザロール
	 */
	public final void setWfUserRoleTransfer(final WfUserRole val) {
		this.wfUserRoleTransfer = val;
	}

	/**
	 * 遷移先プロセス定義コード枝番を取得する.
	 * @return 遷移先プロセス定義コード
	 */
	public final String getProcessDefDetailCodeTransfer() {
		return processDefDetailCodeTransfer;
	}

	/**
	 * 遷移先プロセス定義コード枝番を設定する.
	 * @param pProcessDefDetailCodeTransfer 設定する遷移先プロセス定義コード枝番
	 */
	public final void setProcessDefDetailCodeTransfer(
			final String pProcessDefDetailCodeTransfer) {
		this.processDefDetailCodeTransfer = pProcessDefDetailCodeTransfer;
	}

	/**
	 * 業務管理情報01を設定する.
	 * @param val 業務管理情報01
	 */
	public final void setBusinessInfo01(final String val) {
		this.businessInfo01 = val;
	}
	/**
	 * 業務管理情報01を取得する.
	 * @return 業務管理情報01
	 */
	public final String getBusinessInfo01() {
		return businessInfo01;
	}
	/**
	 * 業務管理情報02を設定する.
	 * @param val 業務管理情報02
	 */
	public final void setBusinessInfo02(final String val) {
		this.businessInfo02 = val;
	}
	/**
	 * 業務管理情報02を取得する.
	 * @return 業務管理情報02
	 */
	public final String getBusinessInfo02() {
		return businessInfo02;
	}
	/**
	 * 業務管理情報03を設定する.
	 * @param val 業務管理情報03
	 */
	public final void setBusinessInfo03(final String val) {
		this.businessInfo03 = val;
	}
	/**
	 * 業務管理情報03を取得する.
	 * @return 業務管理情報03
	 */
	public final String getBusinessInfo03() {
		return businessInfo03;
	}
	/**
	 * 業務管理情報04を設定する.
	 * @param val 業務管理情報04
	 */
	public final void setBusinessInfo04(final String val) {
		this.businessInfo04 = val;
	}
	/**
	 * 業務管理情報04を取得する.
	 * @return 業務管理情報04
	 */
	public final String getBusinessInfo04() {
		return businessInfo04;
	}
	/**
	 * 業務管理情報05を設定する.
	 * @param val 業務管理情報05
	 */
	public final void setBusinessInfo05(final String val) {
		this.businessInfo05 = val;
	}
	/**
	 * 業務管理情報05を取得する.
	 * @return 業務管理情報05
	 */
	public final String getBusinessInfo05() {
		return businessInfo05;
	}
	/**
	 * 業務管理情報06を設定する.
	 * @param val 業務管理情報06
	 */
	public final void setBusinessInfo06(final String val) {
		this.businessInfo06 = val;
	}
	/**
	 * 業務管理情報06を取得する.
	 * @return 業務管理情報06
	 */
	public final String getBusinessInfo06() {
		return businessInfo06;
	}
	/**
	 * 業務管理情報07を設定する.
	 * @param val 業務管理情報07
	 */
	public final void setBusinessInfo07(final String val) {
		this.businessInfo07 = val;
	}
	/**
	 * 業務管理情報07を取得する.
	 * @return 業務管理情報07
	 */
	public final String getBusinessInfo07() {
		return businessInfo07;
	}
	/**
	 * 業務管理情報08を設定する.
	 * @param val 業務管理情報08
	 */
	public final void setBusinessInfo08(final String val) {
		this.businessInfo08 = val;
	}
	/**
	 * 業務管理情報08を取得する.
	 * @return 業務管理情報08
	 */
	public final String getBusinessInfo08() {
		return businessInfo08;
	}
	/**
	 * 業務管理情報09を設定する.
	 * @param val 業務管理情報09
	 */
	public final void setBusinessInfo09(final String val) {
		this.businessInfo09 = val;
	}
	/**
	 * 業務管理情報09を取得する.
	 * @return 業務管理情報09
	 */
	public final String getBusinessInfo09() {
		return businessInfo09;
	}
	/**
	 * 業務管理情報10を設定する.
	 * @param val 業務管理情報10
	 */
	public final void setBusinessInfo10(final String val) {
		this.businessInfo10 = val;
	}
	/**
	 * 業務管理情報10を取得する.
	 * @return 業務管理情報10
	 */
	public final String getBusinessInfo10() {
		return businessInfo10;
	}
	/**
	 * 遷移先会社コードを取得する.
	 * @return 遷移先会社コード

	 */
	public final String getCorporationCodeTransfer() {
		return corporationCodeTransfer;
	}
	/**
	 * 遷移先会社コードを設定する.
	 * @param pCorporationCodeTransfer 遷移先会社コード

	 */
	public final void setCorporationCodeTransfer(final String pCorporationCodeTransfer) {
		this.corporationCodeTransfer = pCorporationCodeTransfer;
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
	 * 連携先プロセス定義リストを取得する.
	 * @return 連携先プロセス定義リスト
	 */
	public final List<WfmProcessDef> getNextWfmProcessDefList() {
		return nextWfmProcessDefList;
	}
	/**
	 * 連携先プロセス定義リストを設定する.
	 * @param pNnextWfmProcessDefList 連携先プロセス定義リスト
	 */
	public final void setNextWfmProcessDefList(final List<WfmProcessDef> pNnextWfmProcessDefList) {
		this.nextWfmProcessDefList = pNnextWfmProcessDefList;
	}

	/**
	 * (新)起票組織コードを取得する.
	 * @return (新)起票組織コード
	 */
	public String getNewOrganizationCodeStart() {
		return newOrganizationCodeStart;
	}

	/**
	 * (新)起票組織コードを設定する.
	 * @param newOrganizationCodeStart (新)起票組織コード
	 */
	public void setNewOrganizationCodeStart(String newOrganizationCodeStart) {
		this.newOrganizationCodeStart = newOrganizationCodeStart;
	}

	/**
	 * (新)起票役職コードを取得する.
	 * @return (新)起票役職コード
	 */
	public String getNewPostCodeStart() {
		return newPostCodeStart;
	}

	/**
	 * (新)起票役職コードを設定する.
	 * @param newPostCodeStart (新)起票役職コード
	 */
	public void setNewPostCodeStart(String newPostCodeStart) {
		this.newPostCodeStart = newPostCodeStart;
	}

	/**
	 * (新)起票者ユーザロールを取得する.
	 * @return (新)起票者ユーザロール
	 */
	public WfUserRole getNewWfUserRoleStart() {
		return newWfUserRoleStart;
	}

	/**
	 * (新)起票者ユーザロールを設定する.
	 * @param newWfUserRoleStart (新)起票者ユーザロール
	 */
	public void setNewWfUserRoleStart(WfUserRole newWfUserRoleStart) {
		this.newWfUserRoleStart = newWfUserRoleStart;
	}

}
