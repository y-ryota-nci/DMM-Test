package jp.co.nci.iwf.component.authenticate;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.iwf.util.MiscUtils;

public class OrganizationInfo implements Serializable {

//	/** '会社コード(CORPORATION_CODE). */
//	@JsonIgnore
//	private String corporationCode;
	/** '組織コード(ORGANIZATION_CODE). */
	private String organizationCode;
	/** '組織名称(ORGANIZATION_NAME). */
	private String organizationName;
	/** '組織付加情報(ORGANIZATION_ADDED_INFO). */
	private String organizationAddedInfo;
	/** '組織略称(ORGANIZATION_NAME_ABBR). */
	private String organizationNameAbbr;
	/** '上位組織コード(ORGANIZATION_CODE_UP). */
	private String organizationCodeUp;
	/** '郵便番号(POST_NUM). */
	private String postNum;
	/** '住所(ADDRESS). */
	private String address;
	/** '電話番号(TEL_NUM). */
	private String telNum;
	/** 'FAX番号(FAX_NUM). */
	private String faxNum;
	/** '組織階層(ORGANIZATION_LEVEL). */
	private Long organizationLevel;
//	/** 'ソート順(SORT_ORDER). */
//	@JsonIgnore
//	private Long sortOrder;
	/** '拡張情報01(EXTENDED_INFO_01). */
	@JsonIgnore
	private String extendedInfo01;
//	/** '拡張情報02(EXTENDED_INFO_02). */
//	@JsonIgnore
//	private String extendedInfo02;
//	/** '拡張情報03(EXTENDED_INFO_03). */
//	@JsonIgnore
//	private String extendedInfo03;
//	/** '拡張情報04(EXTENDED_INFO_04). */
//	@JsonIgnore
//	private String extendedInfo04;
//	/** '拡張情報05(EXTENDED_INFO_05). */
//	@JsonIgnore
//	private String extendedInfo05;
//	/** '拡張情報06(EXTENDED_INFO_06). */
//	@JsonIgnore
//	private String extendedInfo06;
//	/** '拡張情報07(EXTENDED_INFO_07). */
//	@JsonIgnore
//	private String extendedInfo07;
//	/** '拡張情報08(EXTENDED_INFO_08). */
//	@JsonIgnore
//	private String extendedInfo08;
//	/** '拡張情報09(EXTENDED_INFO_09). */
//	@JsonIgnore
//	private String extendedInfo09;
//	/** '拡張情報10(計上コード)(EXTENDED_INFO_10). */
//	@JsonIgnore
//	private String extendedInfo10;
	/** '有効期間開始年月日(VALID_START_DATE). */
	@JsonIgnore
	private Date validStartDate;
	/** '有効期間終了年月日(VALID_END_DATE). */
	@JsonIgnore
	private Date validEndDate;
//	/** 'ID(ID). */
//	@JsonIgnore
//	private Long id;
	/** 上位組織付加情報 */
	private String organizationAddedInfoUp;
	/** 上位組織名称 */
	private String organizationNameUp;
	/** 組織階層名 */
	private String organizationTreeName;


	/** コンストラクタ */
	public OrganizationInfo() {
	}

	/** コンストラクタ */
	public OrganizationInfo(WfcOrganization src) {
		MiscUtils.copyProperties(src, this);
	}

//	/**
//	 * '会社コード(CORPORATION_CODE)'を取得する.
//	 * @return CORPORATION_CODE
//	 */
//	public String getCorporationCode() {
//		return this.corporationCode;
//	}
//
//	/**
//	 * '会社コード(CORPORATION_CODE)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setCorporationCode(final String val) {
//		this.corporationCode = val;
//	}

	/**
	 * '組織コード(ORGANIZATION_CODE)'を取得する.
	 * @return ORGANIZATION_CODE
	 */
	public String getOrganizationCode() {
		return this.organizationCode;
	}

	/**
	 * '組織コード(ORGANIZATION_CODE)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationCode(final String val) {
		this.organizationCode = val;
	}

	/**
	 * '組織名称(ORGANIZATION_NAME)'を取得する.
	 * @return ORGANIZATION_NAME
	 */
	public String getOrganizationName() {
		return this.organizationName;
	}

	/**
	 * '組織名称(ORGANIZATION_NAME)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationName(final String val) {
		this.organizationName = val;
	}

	/**
	 * '組織付加情報(ORGANIZATION_ADDED_INFO)'を取得する.
	 * @return ORGANIZATION_ADDED_INFO
	 */
	public String getOrganizationAddedInfo() {
		return this.organizationAddedInfo;
	}

	/**
	 * '組織付加情報(ORGANIZATION_ADDED_INFO)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationAddedInfo(final String val) {
		this.organizationAddedInfo = val;
	}

	/**
	 * '組織略称(ORGANIZATION_NAME_ABBR)'を取得する.
	 * @return ORGANIZATION_NAME_ABBR
	 */
	public String getOrganizationNameAbbr() {
		return this.organizationNameAbbr;
	}

	/**
	 * '組織略称(ORGANIZATION_NAME_ABBR)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationNameAbbr(final String val) {
		this.organizationNameAbbr = val;
	}

	/**
	 * '上位組織コード(ORGANIZATION_CODE_UP)'を取得する.
	 * @return ORGANIZATION_CODE_UP
	 */
	public String getOrganizationCodeUp() {
		return this.organizationCodeUp;
	}

	/**
	 * '上位組織コード(ORGANIZATION_CODE_UP)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationCodeUp(final String val) {
		this.organizationCodeUp = val;
	}

	/**
	 * '郵便番号(POST_NUM)'を取得する.
	 * @return POST_NUM
	 */
	public String getPostNum() {
		return this.postNum;
	}

	/**
	 * '郵便番号(POST_NUM)'を設定する.
	 * @param val 設定値
	 */
	public void setPostNum(final String val) {
		this.postNum = val;
	}

	/**
	 * '住所(ADDRESS)'を取得する.
	 * @return ADDRESS
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * '住所(ADDRESS)'を設定する.
	 * @param val 設定値
	 */
	public void setAddress(final String val) {
		this.address = val;
	}

	/**
	 * '電話番号(TEL_NUM)'を取得する.
	 * @return TEL_NUM
	 */
	public String getTelNum() {
		return this.telNum;
	}

	/**
	 * '電話番号(TEL_NUM)'を設定する.
	 * @param val 設定値
	 */
	public void setTelNum(final String val) {
		this.telNum = val;
	}

	/**
	 * 'FAX番号(FAX_NUM)'を取得する.
	 * @return FAX_NUM
	 */
	public String getFaxNum() {
		return this.faxNum;
	}

	/**
	 * 'FAX番号(FAX_NUM)'を設定する.
	 * @param val 設定値
	 */
	public void setFaxNum(final String val) {
		this.faxNum = val;
	}

	/**
	 * '組織階層(ORGANIZATION_LEVEL)'を取得する.
	 * @return ORGANIZATION_LEVEL
	 */
	public Long getOrganizationLevel() {
		return this.organizationLevel;
	}

	/**
	 * '組織階層(ORGANIZATION_LEVEL)'を設定する.
	 * @param val 設定値
	 */
	public void setOrganizationLevel(final Long val) {
		this.organizationLevel = val;
	}

//	/**
//	 * 'ソート順(SORT_ORDER)'を取得する.
//	 * @return SORT_ORDER
//	 */
//	public Long getSortOrder() {
//		return this.sortOrder;
//	}
//
//	/**
//	 * 'ソート順(SORT_ORDER)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setSortOrder(final Long val) {
//		this.sortOrder = val;
//	}
//
	/**
	 * '拡張情報01(契約主体資本金金額)(EXTENDED_INFO_01)'を取得する.
	 * @return EXTENDED_INFO_01
	 */
	public String getExtendedInfo01() {
		return this.extendedInfo01;
	}

	/**
	 * '拡張情報01(契約主体資本金金額)(EXTENDED_INFO_01)'を設定する.
	 * @param val 設定値
	 */
	public void setExtendedInfo01(final String val) {
		this.extendedInfo01 = val;
	}

//	/**
//	 * '拡張情報02(EXTENDED_INFO_02)'を取得する.
//	 * @return EXTENDED_INFO_02
//	 */
//	public String getExtendedInfo02() {
//		return this.extendedInfo02;
//	}
//
//	/**
//	 * '拡張情報02(EXTENDED_INFO_02)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo02(final String val) {
//		this.extendedInfo02 = val;
//	}
//
//	/**
//	 * '拡張情報03(職務権限表対象フラグ)(EXTENDED_INFO_03)'を取得する.
//	 * @return EXTENDED_INFO_03
//	 */
//	public String getExtendedInfo03() {
//		return this.extendedInfo03;
//	}
//
//	/**
//	 * '拡張情報03(職務権限表対象フラグ)(EXTENDED_INFO_03)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo03(final String val) {
//		this.extendedInfo03 = val;
//	}
//
//	/**
//	 * '拡張情報04(権限委譲表対象フラグ)(EXTENDED_INFO_04)'を取得する.
//	 * @return EXTENDED_INFO_04
//	 */
//	public String getExtendedInfo04() {
//		return this.extendedInfo04;
//	}
//
//	/**
//	 * '拡張情報04(権限委譲表対象フラグ)(EXTENDED_INFO_04)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo04(final String val) {
//		this.extendedInfo04 = val;
//	}
//
//	/**
//	 * '拡張情報05(EXTENDED_INFO_05)'を取得する.
//	 * @return EXTENDED_INFO_05
//	 */
//	public String getExtendedInfo05() {
//		return this.extendedInfo05;
//	}
//
//	/**
//	 * '拡張情報05(EXTENDED_INFO_05)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo05(final String val) {
//		this.extendedInfo05 = val;
//	}
//
//	/**
//	 * '拡張情報06(契約主体区分)(EXTENDED_INFO_06)'を取得する.
//	 * @return EXTENDED_INFO_06
//	 */
//	public String getExtendedInfo06() {
//		return this.extendedInfo06;
//	}
//
//	/**
//	 * '拡張情報06(契約主体区分)(EXTENDED_INFO_06)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo06(final String val) {
//		this.extendedInfo06 = val;
//	}
//
//	/**
//	 * '拡張情報07(システム間連携)(EXTENDED_INFO_07)'を取得する.
//	 * @return EXTENDED_INFO_07
//	 */
//	public String getExtendedInfo07() {
//		return this.extendedInfo07;
//	}
//
//	/**
//	 * '拡張情報07(システム間連携)(EXTENDED_INFO_07)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo07(final String val) {
//		this.extendedInfo07 = val;
//	}
//
//	/**
//	 * '拡張情報08(MDM会社コード)(EXTENDED_INFO_08)'を取得する.
//	 * @return EXTENDED_INFO_08
//	 */
//	public String getExtendedInfo08() {
//		return this.extendedInfo08;
//	}
//
//	/**
//	 * '拡張情報08(MDM会社コード)(EXTENDED_INFO_08)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo08(final String val) {
//		this.extendedInfo08 = val;
//	}
//
//	/**
//	 * '拡張情報09(EXTENDED_INFO_09)'を取得する.
//	 * @return EXTENDED_INFO_09
//	 */
//	public String getExtendedInfo09() {
//		return this.extendedInfo09;
//	}
//
//	/**
//	 * '拡張情報09(EXTENDED_INFO_09)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo09(final String val) {
//		this.extendedInfo09 = val;
//	}
//
//	/**
//	 * '拡張情報10(計上コード)(EXTENDED_INFO_10)'を取得する.
//	 * @return EXTENDED_INFO_10
//	 */
//	public String getExtendedInfo10() {
//		return this.extendedInfo10;
//	}
//
//	/**
//	 * '拡張情報10(計上コード)(EXTENDED_INFO_10)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setExtendedInfo10(final String val) {
//		this.extendedInfo10 = val;
//	}
//
//	/**
//	 * '有効期間開始年月日(VALID_START_DATE)'を取得する.
//	 * @return VALID_START_DATE
//	 */
//	public Date getValidStartDate() {
//		return this.validStartDate;
//	}
//
//	/**
//	 * '有効期間開始年月日(VALID_START_DATE)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setValidStartDate(final Date val) {
//		this.validStartDate = val;
//	}
//
//	/**
//	 * '有効期間終了年月日(VALID_END_DATE)'を取得する.
//	 * @return VALID_END_DATE
//	 */
//	public Date getValidEndDate() {
//		return this.validEndDate;
//	}
//
//	/**
//	 * '有効期間終了年月日(VALID_END_DATE)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setValidEndDate(final Date val) {
//		this.validEndDate = val;
//	}
//
//	/**
//	 * 'ID(ID)'を取得する.
//	 * @return ID
//	 */
//	public Long getId() {
//		return this.id;
//	}
//
//	/**
//	 * 'ID(ID)'を設定する.
//	 * @param val 設定値
//	 */
//	public void setId(final Long val) {
//		this.id = val;
//	}

	/**
	 * 上位組織付加情報を取得します。
	 * @return 上位組織付加情報
	 */
	public String getOrganizationAddedInfoUp() {
	    return organizationAddedInfoUp;
	}

	/**
	 * 上位組織付加情報を設定します。
	 * @param organizationAddedInfoUp 上位組織付加情報
	 */
	public void setOrganizationAddedInfoUp(String organizationAddedInfoUp) {
	    this.organizationAddedInfoUp = organizationAddedInfoUp;
	}

	/**
	 * 上位組織名称を取得する
	 * @return 上位組織コード名称
	 */
	public String getOrganizationNameUp() {
		return organizationNameUp;
	}

	/**
	 * 上位組織名称を設定する
	 * @param organizationNameUp 上位組織名称
	 */
	public void setOrganizationNameUp(String organizationNameUp) {
		this.organizationNameUp = organizationNameUp;
	}

	/** 組織階層名 */
	public String getOrganizationTreeName() {
		return organizationTreeName;
	}

	/** 組織階層名 */
	public void setOrganizationTreeName(String organizationTreeName) {
		this.organizationTreeName = organizationTreeName;
	}
}
