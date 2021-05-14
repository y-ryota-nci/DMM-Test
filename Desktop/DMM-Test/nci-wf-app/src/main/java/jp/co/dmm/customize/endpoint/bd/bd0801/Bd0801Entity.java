package jp.co.dmm.customize.endpoint.bd.bd0801;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 予算入力
 */
@Entity
@Access(AccessType.FIELD)
public class Bd0801Entity extends BaseJpaEntity {
	@Id
	@Column(name="ID")
	public long id;

	@Column(name="COMPANY_CD")
	public String companyCd;

	@Column(name="YR_CD")
	public String yrCd;

	@Column(name="YR_NM")
	public String yrNm;

	@Column(name="ORGANIZATION_CODE")
	public String organizationCode;

	@Column(name="ORGANIZATION_NAME")
	public String organizationName;

	@Column(name="ORGANIZATION_CODE_UP")
	public String organizationCodeUp;

	@Column(name="ORGANIZATION_NAME_UP")
	public String organizationNameUp;

	@Column(name="BGT_ITM_CD")
	public String bgtItmCd;

	@Column(name="BGT_ITM_NM")
	public String bgtItmNm;

	@Column(name="RCV_COST_PAY_TP")
	public String rcvCostPayTp;

	@Column(name="RCV_COST_PAY_TP_NM")
	public String rcvCostPayTpNm;

	@Column(name="BGT_AMT_01")
	public BigDecimal bgtAmt01;

	@Column(name="BGT_AMT_02")
	public BigDecimal bgtAmt02;

	@Column(name="BGT_AMT_03")
	public BigDecimal bgtAmt03;

	@Column(name="BGT_AMT_04")
	public BigDecimal bgtAmt04;

	@Column(name="BGT_AMT_05")
	public BigDecimal bgtAmt05;

	@Column(name="BGT_AMT_06")
	public BigDecimal bgtAmt06;

	@Column(name="BGT_AMT_07")
	public BigDecimal bgtAmt07;

	@Column(name="BGT_AMT_08")
	public BigDecimal bgtAmt08;

	@Column(name="BGT_AMT_09")
	public BigDecimal bgtAmt09;

	@Column(name="BGT_AMT_10")
	public BigDecimal bgtAmt10;

	@Column(name="BGT_AMT_11")
	public BigDecimal bgtAmt11;

	@Column(name="BGT_AMT_12")
	public BigDecimal bgtAmt12;

	@Column(name="BGT_AMT_TTL")
	public BigDecimal bgtAmtTtl;

	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;

	@Column(name="BS_PL_TP")
	public String bsplTp;

	@Column(name="BS_PL_TP_NM")
	public String bsplTpNm;
}
