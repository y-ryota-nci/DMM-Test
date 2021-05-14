package jp.co.dmm.customize.endpoint.py.py0080;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 前払残高一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0080Entity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 支払No(PAY_NO) */
	@Id
	@Column(name="PAY_NO")
	public String payNo;
	/** 会社コード(COMPANY_CD) */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 前払金No(ADVPAY_NO) */
	@Column(name="ADVPAY_NO")
	public String advpayNo;
	/** 前払金ステータス(ADVPAY_STS) */
	@Column(name="ADVPAY_STS")
	public String advpaySts;
	/** 前払金ステータス(ADVPAY_STS_NM) */
	@Column(name="ADVPAY_STS_NM")
	public String advpayStsNm;
	/** 前払件名(PAY_NM) */
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
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 通貨名称 */
	@Column(name="MNY_NM")
	public String mnyNm;
	/** 前払金額(PAY_AMT) */
	@Column(name="PAY_AMT")
	public BigDecimal payAmt;
	/** 充当済金額(ADVPAY_APLY_AMT) */
	@Column(name="ADVPAY_APLY_AMT")
	public BigDecimal advpayAplyAmt;
	/** 充当可能金額(RMN_AMT) */
	@Column(name="RMN_AMT")
	public BigDecimal rmnAmt;
	@Column(name="SCREEN_CODE")
	public String screenCode;

}