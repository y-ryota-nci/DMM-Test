package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Timestamp;

public class PullbackActivityInstanceRequest extends ApiBaseRequest {
	/** 会社コード. */
	private String corporationCode;
	/** プロセスID. */
	private Long processId;
	/** アクティビティID. */
	private Long activityId;
	/** 引戻先会社コード. */
	private String corporationCodePullback;
	/** 引戻先プロセス定義コード. */
	private String processDefCodePullback;
	/** 引戻先プロセス定義コード枝番. */
	private String processDefDetailCodePullback;
	/** 引戻先アクティビティ定義コード. */
	private String activityDefCodePullback;
	/** アクションコード. */
	private String actionCode;
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
	/** 処理者組織コード */
	private String organizationCodeOperation;
	/** 処理者組織名 */
	private String organizationNameOperation;
	/** 処理者役職コード */
	private String postCodeOperation;
	/** 処理者役職名 */
	private String postNameOperation;

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
	 * 引戻先アクティビティ定義コードを取得する.
	 * @return 引戻先アクティビティ定義コード

	 */
	public final String getActivityDefCodePullback() {
		return activityDefCodePullback;
	}
	/**
	 * 引戻先アクティビティ定義コードを設定する.
	 * @param pActivityDefCodePullback 引戻先アクティビティ定義コード

	 */
	public final void setActivityDefCodePullback(final String pActivityDefCodePullback) {
		this.activityDefCodePullback = pActivityDefCodePullback;
	}
	/**
	 * 引戻先会社コードを取得する.
	 * @return 引戻先会社コード

	 */
	public final String getCorporationCodePullback() {
		return corporationCodePullback;
	}
	/**
	 * 引戻先会社コードを設定する.
	 * @param pCorporationCodePullback 引戻先会社コード

	 */
	public final void setCorporationCodePullback(final String pCorporationCodePullback) {
		this.corporationCodePullback = pCorporationCodePullback;
	}
	/**
	 * 引戻先プロセス定義コードを取得する.
	 * @return 引戻先プロセス定義コード

	 */
	public final String getProcessDefCodePullback() {
		return processDefCodePullback;
	}
	/**
	 * 引戻先プロセス定義コードを設定する.
	 * @param pProcessDefCodePullback 引戻先プロセス定義コード

	 */
	public final void setProcessDefCodePullback(final String pProcessDefCodePullback) {
		this.processDefCodePullback = pProcessDefCodePullback;
	}
	/**
	 * プロセス定義コード枝番を取得する.
	 * @return プロセス定義コード枝番
	 */
	public final String getProcessDefDetailCodePullback() {
		return processDefDetailCodePullback;
	}
	/**
	 * プロセス定義コード枝番を設定する.
	 * @param pProcessDefDetailCodePullback プロセス定義コード枝番
	 */
	public final void setProcessDefDetailCodePullback(final String pProcessDefDetailCodePullback) {
		this.processDefDetailCodePullback = pProcessDefDetailCodePullback;
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
	 * 処理者組織コードを取得する
	 * @return 処理者組織コード
	 */
	public String getOrganizationCodeOperation() {
		return organizationCodeOperation;
	}

	/**
	 * 処理者組織コードを設定する
	 * @param organizationCodeOperation 処理者組織コード
	 */
	public void setOrganizationCodeOperation(String organizationCodeOperation) {
		this.organizationCodeOperation = organizationCodeOperation;
	}

	/**
	 * 処理者組織名を取得する
	 * @return 処理者組織名
	 */
	public String getOrganizationNameOperation() {
		return organizationNameOperation;
	}

	/**
	 * 処理者組織名を設定する
	 * @param organizationNameOperation 処理者組織名
	 */
	public void setOrganizationNameOperation(String organizationNameOperation) {
		this.organizationNameOperation = organizationNameOperation;
	}

	/**
	 * 処理者役職コードを取得する
	 * @return 処理者役職コード
	 */
	public String getPostCodeOperation() {
		return postCodeOperation;
	}

	/**
	 * 処理者役職コードを設定する
	 * @param postCodeOperation 処理者役職コード
	 */
	public void setPostCodeOperation(String postCodeOperation) {
		this.postCodeOperation = postCodeOperation;
	}

	/**
	 * 処理者役職名を取得する
	 * @return 処理者役職名
	 */
	public String getPostNameOperation() {
		return postNameOperation;
	}

	/**
	 * 処理者役職名を設定する
	 * @param postNameOperation 処理者役職名
	 */
	public void setPostNameOperation(String postNameOperation) {
		this.postNameOperation = postNameOperation;
	}
}
