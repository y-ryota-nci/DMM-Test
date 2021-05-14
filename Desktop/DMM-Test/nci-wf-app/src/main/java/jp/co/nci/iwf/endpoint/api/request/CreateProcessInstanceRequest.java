package jp.co.nci.iwf.endpoint.api.request;

import java.sql.Date;

public class CreateProcessInstanceRequest extends ApiBaseRequest {

	/** 会社コード. */
	private String corporationCode;
	/** プロセス定義コード. */
	private String processDefCode;
	/** プロセス定義コード枝番. */
	private String processDefDetailCode;
	/** 起票組織コード. */
	private String organizationCodeStart;
	/** 起票役職コード. */
	private String postCodeStart;
	/** スキップ実行指示. */
	private boolean isSkip;
	/** 画面プロセスID. */
	private Long screenProcessId;
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
	/** 文書ID. */
	private Long docId;
	/** メジャーバージョン. */
	private Long majorVersion;
	/** マイナーバージョン. */
	private Long minorVersion;
	/** プロセス集約ID. */
	private Long processIdAggregation;
	/** 前プロセスID. */
	private Long processIdPrev;
	/** 前アクティビティID. */
	private Long activityIdPrev;
	/** 前アクティビティID. */
	private Long seqNoWfRelationDef;
	/** プロセス起票区分. */
	private String createProcessType;
	/** 起票日. */
	private Date startDate;
	/** 検索分類. */
	private String searchCategory;

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
		return this.processDefCode;
	}

	/**
	 * プロセス定義コードを設定する.
	 * @param val プロセス定義コード

	 */
	public final void setProcessDefCode(final String val) {
		this.processDefCode = val;
	}

	/**
	 * スキップ実行指示を判定する.
	 * @return the isSkip
	 */
	public final boolean isSkip() {
		return isSkip;
	}
	/**
	 * スキップ実行指示を設定する.
	 * @param pIsSkip the isSkip to set
	 */
	public final void setSkip(final boolean pIsSkip) {
		this.isSkip = pIsSkip;
	}
	/**
	 * @return screenProcessId
	 */
	public Long getScreenProcessId() {
		return screenProcessId;
	}
	/**
	 * @param screenProcessId セットする screenProcessId
	 */
	public void setScreenProcessId(Long screenProcessId) {
		this.screenProcessId = screenProcessId;
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
	 * 文書IDを取得する.
	 * @return
	 */
	public Long getDocId() {
		return docId;
	}

	/**
	 * 文書IDを設定する.
	 * @param docId 文書ID
	 */
	public void setDocId(Long docId) {
		this.docId = docId;
	}

	/**
	 * メジャーバージョンを取得する.
	 * @return メジャーバージョン
	 */
	public Long getMajorVersion() {
		return majorVersion;
	}

	/**
	 * メジャーバージョンを設定する.
	 * @param majorVersion メジャーバージョン
	 */
	public void setMajorVersion(Long majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * マイナーバージョンを取得する.
	 * @return マイナーバージョン
	 */
	public Long getMinorVersion() {
		return minorVersion;
	}

	/**
	 * マイナーバージョンを設定する.
	 * @param minorVersion マイナーバージョン
	 */
	public void setMinorVersion(Long minorVersion) {
		this.minorVersion = minorVersion;
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
	 * プロセス集約IDを取得する.
	 * @return プロセス集約ID
	 */
	public final Long getProcessIdAggregation() {
		return processIdAggregation;
	}

	/**
	 * プロセス集約IDを設定する.
	 * @param val 設定するプロセス集約ID
	 */
	public final void setProcessIdAggregation(final Long val) {
		this.processIdAggregation = val;
	}

	/**
	 * 起票組織コードを取得する.
	 * @return 起票組織コード

	 */
	public final String getOrganizationCodeStart() {
		return organizationCodeStart;
	}

	/**
	 * 起票組織コードを設定する.
	 * @param val 設定する起票組織コード

	 */
	public final void setOrganizationCodeStart(final String val) {
		this.organizationCodeStart = val;
	}

	/**
	 * 起票役職コードを取得する.
	 * @return 起票役職コード

	 */
	public final String getPostCodeStart() {
		return postCodeStart;
	}
	/**
	 * 起票役職コードを設定する.
	 * @param pPostCodeStart 設定する起票役職コード

	 */
	public final void setPostCodeStart(final String pPostCodeStart) {
		this.postCodeStart = pPostCodeStart;
	}
	/**
	 * 前プロセスIDを取得する.
	 * @return 前プロセスID
	 */
	public final Long getProcessIdPrev() {
		return processIdPrev;
	}
	/**
	 * 前プロセスIDを設定する.
	 * @param pProcessIdPrev 前プロセスID
	 */
	public final void setProcessIdPrev(final Long pProcessIdPrev) {
		this.processIdPrev = pProcessIdPrev;
	}
	/**
	 * 前アクティビティIDを取得する.
	 * @return 前アクティビティID
	 */
	public final Long getActivityIdPrev() {
		return activityIdPrev;
	}
	/**
	 * 前アクティビティIDを設定する.
	 * @param pActivityIdPrev 前アクティビティID
	 */
	public final void setActivityIdPrev(final Long pActivityIdPrev) {
		this.activityIdPrev = pActivityIdPrev;
	}
	/**
	 * プロセス連携定義連番を取得する.
	 * @return 前アクティビティID
	 */
	public final Long getSeqNoWfRelationDef() {
		return this.seqNoWfRelationDef;
	}
	/**
	 * プロセス連携定義連番を設定する.
	 * @param pSeqNoWfRelationDef 前アクティビティID
	 */
	public final void setSeqNoWfRelationDef(final Long pSeqNoWfRelationDef) {
		this.seqNoWfRelationDef = pSeqNoWfRelationDef;
	}
	/**
	 * プロセス起票区分を取得する.
	 * @return プロセス起票区分
	 */
	public final String getCreateProcessType() {
		return createProcessType;
	}
	/**
	 * プロセス起票区分を設定する.
	 * @param pCreateProcessType プロセス起票区分
	 */
	public final void setCreateProcessType(final String pCreateProcessType) {
		this.createProcessType = pCreateProcessType;
	}
	/**
	 * 起票日を取得する.
	 * @return 起票日
	 */
	public final Date getStartDate() {
		return startDate;
	}
	/**
	 * 起票日を設定する.
	 * @param pStartDate 起票日
	 */
	public final void setStartDate(final Date pStartDate) {
		this.startDate = pStartDate;
	}
	/**
	 * 検索分類を取得する.
	 * @return searchCategory
	 */
	public String getSearchCategory() {
		return searchCategory;
	}
	/**
	 * 検索分類を設定する.
	 * @param searchCategory セットする searchCategory
	 */
	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

}
