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
 * プロファイル情報アップロードの「役職」シートの「役職」一行分を表すBean
 */
@Entity(name="WFM_POST_V")
@Access(AccessType.FIELD)
public class Up0200Post extends BaseJpaEntity {
	/** 企業コード */
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	/** 役職コード */
	@Column(name="POST_CODE")
	public String postCode;

	/** 役職名(日本語) */
	@Column(name="POST_NAME_JA")
	public String postNameJa;

	/** 役職名(英語) */
	@Column(name="POST_NAME_EN")
	public String postNameEn;

	/** 役職名(中国語) */
	@Column(name="POST_NAME_ZH")
	public String postNameZh;

	/** 役職付加情報 */
	@Column(name="POST_ADDED_INFO")
	public String postAddedInfo;

	/** 役職略称(日本語) */
	@Column(name="POST_NAME_ABBR_JA")
	public String postNameAbbrJa;

	/** 役職略称(英語) */
	@Column(name="POST_NAME_ABBR_EN")
	public String postNameAbbrEn;

	/** 役職略称(中国語) */
	@Column(name="POST_NAME_ABBR_ZH")
	public String postNameAbbrZh;

	/** 役職階層 */
	@Column(name="POST_LEVEL")
	public Long postLevel;

	/** 有効開始日 */
	@Column(name="VALID_START_DATE")
	public Date validStartDate;

	/** 有効終了日 */
	@Column(name="VALID_END_DATE")
	public Date validEndDate;

	/** 上位役職者設定フラグ */
	@Column(name="UPPER_POST_SETTINGS_FLAG")
	public String uppperPostSettingsFlag;

	/** 削除フラグ */
	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	/** ID */
	@Column(name="ID")
	@Id
	public Long id;

	/** エラー内容*/
	@Transient
	public String errorText;

	// 2019/11/05 Excelダウンロード時の対応
	//	他の型だとエラー時正常にエラーを出力できないことがあるためStringでない型の変数のString版
	/** '有効期間開始年月日 */
	public String strValidStartDate;
	/** '有効期間終了年月日 */
	public String strValidEndDate;
	/** '役職階層 */
	public String strPostLevel;
}
