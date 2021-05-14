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
 * プロファイル情報アップロードの「ユーザ」シートのユーザ一行分を表すBean
 */
@Entity(name="WFM_USER_V")
@Access(AccessType.FIELD)
public class Up0200User extends BaseJpaEntity {
	/** 企業コード */
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	/** ユーザコード */
	@Column(name="USER_CODE")
	public String userCode;

	/** ユーザ氏名(日本語) */
	@Column(name="USER_NAME_JA")
	public String userNameJa;

	/** ユーザ氏名(英語) */
	@Column(name="USER_NAME_EN")
	public String userNameEn;

	/** ユーザ氏名(中国語) */
	@Column(name="USER_NAME_ZH")
	public String userNameZh;

	/** ユーザ略称(日本語) */
	@Column(name="USER_NAME_ABBR_JA")
	public String userNameAbbrJa;

	/** ユーザ略称(英語) */
	@Column(name="USER_NAME_ABBR_EN")
	public String userNameAbbrEn;

	/** ユーザ略称(中国語) */
	@Column(name="USER_NAME_ABBR_ZH")
	public String userNameAbbrZh;

	/** 郵便番号*/
	@Column(name="POST_NUM")
	public String postNum;

	/** 住所(日本語) */
	@Column(name="ADDRESS_JA")
	public String addressJa;

	/** 住所(英語) */
	@Column(name="ADDRESS_EN")
	public String addressEn;

	/** 住所(中国語)*/
	@Column(name="ADDRESS_ZH")
	public String addressZh;

	/** 電話番号 */
	@Column(name="TEL_NUM")
	public String telNum;

	/** 携帯電話番号 */
	@Column(name="TEL_NUM_CEL")
	public String telNumCel;

	/** メールアドレス */
	@Column(name="MAIL_ADDRESS")
	public String mailAddress;

	/** ユーザID */
	@Column(name="USER_ADDED_INFO")
	public String userAddedInfo;

	/** 印章名称 */
	@Column(name="SEAL_NAME")
	public String sealName;

	/** 管理者区分 */
	@Column(name="ADMINISTRATOR_TYPE")
	public String administratorType;

	/** デフォルト言語コード */
	@Column(name="DEFAULT_LOCALE_CODE")
	public String defaultLocaleCode;

	/** 拡張情報01 */
	@Column(name="EXTENDED_INFO_01")
	public String extendedInfo01;

	/** 拡張情報02 */
	@Column(name="EXTENDED_INFO_02")
	public String extendedInfo02;

	/** 拡張情報03*/
	@Column(name="EXTENDED_INFO_03")
	public String extendedInfo03;

	/** 拡張情報04 */
	@Column(name="EXTENDED_INFO_04")
	public String extendedInfo04;

	/** 拡張情報05 */
	@Column(name="EXTENDED_INFO_05")
	public String extendedInfo05;

	/** 拡張情報06 */
	@Column(name="EXTENDED_INFO_06")
	public String extendedInfo06;

	/** 拡張情報07 */
	@Column(name="EXTENDED_INFO_07")
	public String extendedInfo07;

	/** 拡張情報08 */
	@Column(name="EXTENDED_INFO_08")
	public String extendedInfo08;

	/** 拡張情報09 */
	@Column(name="EXTENDED_INFO_09")
	public String extendedInfo09;

	/** 拡張情報10 */
	@Column(name="EXTENDED_INFO_10")
	public String extendedInfo10;

	/** 有効開始日 */
	@Column(name="VALID_START_DATE")
	public Date validStartDate;

	/** 有効終了日 */
	@Column(name="VALID_END_DATE")
	public Date validEndDate;

	/** 削除フラグ */
	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	/** ID */
	@Column(name="ID")
	@Id
	public Long id;

	@Transient
	public String errorText;

	// 2019/11/05 Excelダウンロード時の対応
	//	他の型だとエラー時正常にエラーを出力できないことがあるためStringでない型の変数のString版
	/** '有効期間開始年月日 */
	public String strValidStartDate;
	/** '有効期間終了年月日 */
	public String strValidEndDate;
}
