package jp.co.nci.iwf.endpoint.up.up0200.entity;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * プロファイル情報アップロードの「組織」シートの組織一行分を表すBean
 */
@Entity(name="WFM_ORGANIZATION_V")
@Access(AccessType.FIELD)
public class Up0200Organization extends BaseJpaEntity {
	/** '会社コード */
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	/** '組織コード */
	@Column(name="ORGANIZATION_CODE")
	public String organizationCode;

	/** '組織名（日本語） */
	@Column(name="ORGANIZATION_NAME_JA")
	public String organizationNameJa;

	/** '組織名(英語) */
	@Column(name="ORGANIZATION_NAME_EN")
	public String organizationNameEn;

	/** '組織名(中国) */
	@Column(name="ORGANIZATION_NAME_ZH")
	public String organizationNameZh;

	/** '組織付加情報 */
	@Column(name="ORGANIZATION_ADDED_INFO")
	public String organizationAddedInfo;

	/** '組織略称(日本語). */
	@Column(name="ORGANIZATION_NAME_ABBR_JA")
	public String organizationNameAbbrJa;

	/** '組織略称(英語). */
	@Column(name="ORGANIZATION_NAME_ABBR_EN")
	public String organizationNameAbbrEn;

	/** '組織略称(中国語). */
	@Column(name="ORGANIZATION_NAME_ABBR_ZH")
	public String organizationNameAbbrZh;

	/** '上位組織コード */
	@Column(name="ORGANIZATION_CODE_UP")
	public String organizationCodeUp;

	/** '郵便番号 */
	@Column(name="POST_NUM")
	public String postNum;

	/** '住所 (日本語)*/
	@Column(name="ADDRESS_JA")
	public String addressJa;

	/** '住所 (英語)*/
	@Column(name="ADDRESS_EN")
	public String addressEn;

	/** '住所 (中国語)*/
	@Column(name="ADDRESS_ZH")
	public String addressZh;

	/** '電話番号 */
	@Column(name="TEL_NUM")
	public String telNum;

	/** 'FAX番号 */
	@Column(name="FAX_NUM")
	public String faxNum;

	/** '組織階層 */
	@Column(name="ORGANIZATION_LEVEL")
	public Long organizationLevel;

	/** 'ソート順 */
	@Column(name="SORT_ORDER")
	public Long sortOrder;

	/** '拡張情報01 */
	@Column(name="EXTENDED_INFO_01")
	public String extendedInfo01;

	/** '拡張情報02 */
	@Column(name="EXTENDED_INFO_02")
	public String extendedInfo02;

	/** '拡張情報03 */
	@Column(name="EXTENDED_INFO_03")
	public String extendedInfo03;

	/** '拡張情報04 */
	@Column(name="EXTENDED_INFO_04")
	public String extendedInfo04;

	/** '拡張情報05 */
	@Column(name="EXTENDED_INFO_05")
	public String extendedInfo05;

	/** '拡張情報06 */
	@Column(name="EXTENDED_INFO_06")
	public String extendedInfo06;

	/** '拡張情報07 */
	@Column(name="EXTENDED_INFO_07")
	public String extendedInfo07;

	/** '拡張情報08 */
	@Column(name="EXTENDED_INFO_08")
	public String extendedInfo08;

	/** '拡張情報09 */
	@Column(name="EXTENDED_INFO_09")
	public String extendedInfo09;

	/** '拡張情報10 */
	@Column(name="EXTENDED_INFO_10")
	public String extendedInfo10;

	/** '有効期間開始年月日 */
	@Column(name="VALID_START_DATE")
	public Date validStartDate;

	/** '有効期間終了年月日 */
	@Column(name="VALID_END_DATE")
	public Date validEndDate;

	/** '削除フラグ */
	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	/** JPA的には不要だが、アップロードに必要 */
	@Transient
	public String errorText;

	/** ID（アップロードには不要だが、JPAのエンティティ的には必須なので仕方なく...） */
	@Column(name="ID")
	@Id
	public Long id;

	// 2019/11/05 Excelダウンロード時の対応
	//	他の型だとエラー時正常にエラーを出力できないことがあるためStringでない型の変数のString版
	/** '有効期間開始年月日 */
	public String strValidStartDate;
	/** '有効期間終了年月日 */
	public String strValidEndDate;
	/** '組織階層 */
	public String strOrganizationLevel;
	/** 'ソート順 */
	public String strSortOrder;
}
