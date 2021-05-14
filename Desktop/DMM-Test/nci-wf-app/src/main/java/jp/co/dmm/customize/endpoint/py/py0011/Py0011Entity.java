package jp.co.dmm.customize.endpoint.py.py0011;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 支払一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0011Entity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	public Long id;
	@Column(name="COMPANY_CD")
	public String companyCd;
	@Column(name="CRDCRD_IN_NO")
	public String crdcrdInNo;
	@Column(name="PAY_YM")
	public String payYm;
	@Column(name="SPLR_CD")
	public String splrCd;
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	@Column(name="CRD_COMPANY_NM")
	public String crdCompanyNm;
	@Column(name="USR_CD")
	public String usrCd;
	@Column(name="USR_NM")
	public String usrNm;
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	@Temporal(TemporalType.DATE)
	@Column(name="USE_DT")
	public Date useDt;
	@Column(name="AMT_JPY")
	public BigDecimal amtJpy;
	@Column(name="USE_")
	public String use;
	@Column(name="AMT_FC")
	public String amtFc;
	@Column(name="RCVINSP_NO")
	public String rcvinspNo;
	@Column(name="RCVINSP_DTL_NO")
	public Long rcvinspDtlNo;
	@Column(name="DISP_RCVINSP_NO")
	public String dispRcvinspNo;
	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;
	@Column(name="ITMEXPS_NM1")
	public String itmexpsNm1;
	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;
	@Column(name="ITMEXPS_NM2")
	public String itmexpsNm2;
	@Column(name="ITM_CD")
	public String itmCd;
	@Column(name="ITM_NM")
	public String itmNm;
	@Column(name="BUMON_CD")
	public String bumonCd;
	@Column(name="PAY_SMRY")
	public String paySmry;
	@Column(name="MAT_STS")
	public String matSts;
	@Column(name="MAT_STS_NM")
	public String matStsNm;
	@Column(name="USE_DTL_ITM1")
	public String useDtlItm1;
	@Column(name="USE_DTL_ITM2")
	public String useDtlItm2;
	@Column(name="USE_DTL_ITM3")
	public String useDtlItm3;
	@Column(name="USE_DTL_ITM4")
	public String useDtlItm4;
	@Column(name="USE_DTL_ITM5")
	public String useDtlItm5;
	@Column(name="USE_DTL_ITM6")
	public String useDtlItm6;
	@Column(name="USE_DTL_ITM7")
	public String useDtlItm7;
	@Column(name="USE_DTL_ITM8")
	public String useDtlItm8;
	@Column(name="USE_DTL_ITM9")
	public String useDtlItm9;
	@Column(name="USE_DTL_ITM10")
	public String useDtlItm10;
	@Column(name="USE_DTL_ITM11")
	public String useDtlItm11;
	@Column(name="USE_DTL_ITM12")
	public String useDtlItm12;
	@Column(name="USE_DTL_ITM13")
	public String useDtlItm13;
	@Column(name="USE_DTL_ITM14")
	public String useDtlItm14;
	@Column(name="USE_DTL_ITM15")
	public String useDtlItm15;
	@Column(name="USE_DTL_ITM16")
	public String useDtlItm16;
	@Column(name="USE_DTL_ITM17")
	public String useDtlItm17;
	@Column(name="USE_DTL_ITM18")
	public String useDtlItm18;
	@Column(name="USE_DTL_ITM19")
	public String useDtlItm19;
	@Column(name="USE_DTL_ITM20")
	public String useDtlItm20;
	@Column(name="USE_DTL_ITM21")
	public String useDtlItm21;
	@Column(name="USE_DTL_ITM22")
	public String useDtlItm22;
	@Column(name="USE_DTL_ITM23")
	public String useDtlItm23;
	@Column(name="USE_DTL_ITM24")
	public String useDtlItm24;
	@Column(name="USE_DTL_ITM25")
	public String useDtlItm25;
	@Column(name="USE_DTL_ITM26")
	public String useDtlItm26;

}
