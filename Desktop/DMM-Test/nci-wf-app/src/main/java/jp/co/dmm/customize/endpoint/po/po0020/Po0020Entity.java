package jp.co.dmm.customize.endpoint.po.po0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 管理_定期発注マスタ一覧エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Po0020Entity extends BaseJpaEntity {
	@Id
	@Column(name="ID")
	public long id;

	/** 会社CD(COMPANY_CD) */
	@Column(name="COMPANY_CD")
	public String companyCd;

	/** 取引先コード(SPLR_CD) */
	@Column(name="SPLR_CD")
	public String splrCd;

	/** 取引先名（漢字）(SPLR_NM_KJ) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;

	@Column(name="PURORD_NO")
	public String purordNo;

	@Column(name="PRD_PURORD_NO")
	public String prdPurordNo;

	@Column(name="CNTRCT_NO")
	public String cntrctNo;

	@Column(name="ML_ADD_TP")
	public String mlAddTp;

	@Column(name="ML_ADD_TP_NM")
	public String mlAddTpNm;

	@Column(name="PAY_START_TIME")
	public String payStartTime;

	@Column(name="PAY_END_TIME")
	public String payEndTime;
}
