package jp.co.dmm.customize.endpoint.py.py0060;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 前払一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0060Entity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 支払No(PAY_NO) */
	@Id
	@Column(name="PAY_NO")
	public String payNo;
	/** 会社コード(COMPANY_CD) */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 支払ステータス(PAY_STS) */
	@Column(name="PAY_STS")
	public String paySts;
	/** 支払ステータス(PAY_STS_NM) */
	@Column(name="PAY_STS_NM")
	public String payStsNm;
	/** 支払件名(PAY_NM) */
	@Column(name="PAY_NM")
	public String payNm;
	/** 取引先コード(SPLR_CD) */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名（漢字）(SPLR_NM_KJ) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名（カタカナ）(SPLR_NM_KN) */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 支払金額（税抜）(PAY_AMT_EXCTAX) */
	@Column(name="PAY_AMT_EXCTAX")
	public BigDecimal payAmtExctax;
	/** 支払金額（税込）(PAY_AMT_INCTAX) */
	@Column(name="PAY_AMT_INCTAX")
	public BigDecimal payAmtInctax;
	/** 支払依頼日(PAY_RQST_DT) */
	@Column(name="PAY_RQST_DT")
	public Date payRqstDt;
	/** 支払予定日(PAY_PLN_DT) */
	@Column(name="PAY_PLN_DT")
	public Date payPlnDt;
	/** 通貨区分名称(MNY_TP_NM) */
	@Column(name="MNY_TP_NM")
	public String mnyTpNm;
	@Column(name="SCREEN_CODE")
	public String screenCode;

}
