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
 * プロファイル情報アップロードの「ユーザ所属」シートのユーザ所属一行分を表すBean
 */
@Entity(name="WFM_USER_BELONG")
@Access(AccessType.FIELD)
public class Up0200UserBelong extends BaseJpaEntity {
	/** 企業コード */
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	/** ユーザコード */
	@Column(name="USER_CODE")
	public String userCode;

	/** 所属連番 */
	@Column(name="SEQ_NO_USER_BELONG")
	public Long seqNoUserBelong;

	/** 組織コード */
	@Column(name="ORGANIZATION_CODE")
	public String organizationCode;

	/** 役職コード */
	@Column(name="POST_CODE")
	public String postCode;

	/** 主務兼務 */
	@Column(name="JOB_TYPE")
	public String jobType;

	/** 所属長フラグ */
	@Column(name="IMMEDIATE_MANAGER_FLAG")
	public String immediateManagerFlag;

	/** 責任者フラグ */
	@Column(name="DIRECTOR_FLAG")
	public String directorFlag;

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

	/** エラー内容 */
	@Transient
	public String errorText;

	// 2019/11/05 Excelダウンロード時の対応
	//	他の型だとエラー時正常にエラーを出力できないことがあるためStringでない型の変数のString版
	/** '有効期間開始年月日 */
	public String strValidStartDate;
	/** '有効期間終了年月日 */
	public String strValidEndDate;
	/** '所属連番 */
	public String strSeqNoUserBelong;
}
