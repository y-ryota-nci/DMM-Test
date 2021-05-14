package jp.co.dmm.customize.endpoint.po.po0010;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 発注一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Po0010Entity extends BaseJpaEntity {
	/** 発注No */
	@Id
	@Column(name="PURORD_NO")
	public String purordNo;
	/** 会社CD */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 発注件名 */
	@Column(name="PURORD_NM")
	public String purordNm;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名（カタカナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 発注申請者コード */
	public String sbmtrCd;
	/** 発注依頼日From */
	@Column(name="PURORD_RQST_DT")
	public Date purordRqstDt;
	/** 購入依頼No */
	@Column(name="PURRQST_NO")
	public String purrqstNo;
	/** 契約書No */
	@Column(name="CNTRCT_NO")
	public String cntrctNo;
	/** 発注区分 */
	@Column(name="PURORD_TP")
	public String purordTp;
	/** 定期発注区分 */
	@Column(name="PRD_PURORD_TP")
	public String prdPurordTp;
	/** 発注区分名称 */
	@Column(name="PURORD_TP_NM")
	public String purordTpNm;
	/** 納期 */
	@Column(name="DLV_DT")
	public Date dlvDt;
	/** 納入予定日 */
	@Column(name="DLV_PLN_DT")
	public Date dlvPlnDt;
	/** 検査完了期日 */
	@Column(name="INSP_COMP_DT")
	public Date inspCompDt;
	/** 発注ステータス */
	@Column(name="PURORD_STS")
	public String purordSts;
	/** 発注ステータス名 */
	@Column(name="PURORD_STS_NM")
	public String purordStsNm;
	/** 発注金額（税込） */
	@Column(name="PURORD_AMT_INCTAX")
	public BigDecimal purordAmtInctax;
	/** 発注金額（税抜） */
	@Column(name="PURORD_AMT_EXCTAX")
	public BigDecimal purordAmtExctax;
	/** 支払予定日 */
	@Column(name="PAY_PLN_DT")
	public Date payPlnDt;

	@Column(name="SCREEN_CODE")
	public String screenCode;
}
