package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the CRDCRD_INF database table.
 * 
 */
@Entity
@Table(name="CRDCRD_INF")
@NamedQuery(name="CrdcrdInf.findAll", query="SELECT c FROM CrdcrdInf c")
public class CrdcrdInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CrdcrdInfPK id;

	@Column(name="AMT_FC")
	private String amtFc;

	@Column(name="AMT_JPY")
	private BigDecimal amtJpy;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRD_COMPANY_NM")
	private String crdCompanyNm;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_CD")
	private String itmCd;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	@Column(name="MAT_STS")
	private String matSts;

	@Column(name="PAY_SMRY")
	private String paySmry;

	@Column(name="PAY_YM")
	private String payYm;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USE_")
	private String use;

	@Temporal(TemporalType.DATE)
	@Column(name="USE_DT")
	private Date useDt;

	@Column(name="USE_DTL_ITM1")
	private String useDtlItm1;

	@Column(name="USE_DTL_ITM10")
	private String useDtlItm10;

	@Column(name="USE_DTL_ITM11")
	private String useDtlItm11;

	@Column(name="USE_DTL_ITM12")
	private String useDtlItm12;

	@Column(name="USE_DTL_ITM13")
	private String useDtlItm13;

	@Column(name="USE_DTL_ITM14")
	private String useDtlItm14;

	@Column(name="USE_DTL_ITM15")
	private String useDtlItm15;

	@Column(name="USE_DTL_ITM16")
	private String useDtlItm16;

	@Column(name="USE_DTL_ITM17")
	private String useDtlItm17;

	@Column(name="USE_DTL_ITM18")
	private String useDtlItm18;

	@Column(name="USE_DTL_ITM19")
	private String useDtlItm19;

	@Column(name="USE_DTL_ITM2")
	private String useDtlItm2;

	@Column(name="USE_DTL_ITM20")
	private String useDtlItm20;

	@Column(name="USE_DTL_ITM21")
	private String useDtlItm21;

	@Column(name="USE_DTL_ITM22")
	private String useDtlItm22;

	@Column(name="USE_DTL_ITM23")
	private String useDtlItm23;

	@Column(name="USE_DTL_ITM24")
	private String useDtlItm24;

	@Column(name="USE_DTL_ITM25")
	private String useDtlItm25;

	@Column(name="USE_DTL_ITM26")
	private String useDtlItm26;

	@Column(name="USE_DTL_ITM3")
	private String useDtlItm3;

	@Column(name="USE_DTL_ITM4")
	private String useDtlItm4;

	@Column(name="USE_DTL_ITM5")
	private String useDtlItm5;

	@Column(name="USE_DTL_ITM6")
	private String useDtlItm6;

	@Column(name="USE_DTL_ITM7")
	private String useDtlItm7;

	@Column(name="USE_DTL_ITM8")
	private String useDtlItm8;

	@Column(name="USE_DTL_ITM9")
	private String useDtlItm9;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Column(name="USR_CD")
	private String usrCd;

	public CrdcrdInf() {
	}

	public CrdcrdInfPK getId() {
		return this.id;
	}

	public void setId(CrdcrdInfPK id) {
		this.id = id;
	}

	public String getAmtFc() {
		return this.amtFc;
	}

	public void setAmtFc(String amtFc) {
		this.amtFc = amtFc;
	}

	public BigDecimal getAmtJpy() {
		return this.amtJpy;
	}

	public void setAmtJpy(BigDecimal amtJpy) {
		this.amtJpy = amtJpy;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getCrdCompanyNm() {
		return this.crdCompanyNm;
	}

	public void setCrdCompanyNm(String crdCompanyNm) {
		this.crdCompanyNm = crdCompanyNm;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getItmCd() {
		return this.itmCd;
	}

	public void setItmCd(String itmCd) {
		this.itmCd = itmCd;
	}

	public String getItmexpsCd1() {
		return this.itmexpsCd1;
	}

	public void setItmexpsCd1(String itmexpsCd1) {
		this.itmexpsCd1 = itmexpsCd1;
	}

	public String getItmexpsCd2() {
		return this.itmexpsCd2;
	}

	public void setItmexpsCd2(String itmexpsCd2) {
		this.itmexpsCd2 = itmexpsCd2;
	}

	public String getMatSts() {
		return this.matSts;
	}

	public void setMatSts(String matSts) {
		this.matSts = matSts;
	}

	public String getPaySmry() {
		return this.paySmry;
	}

	public void setPaySmry(String paySmry) {
		this.paySmry = paySmry;
	}

	public String getPayYm() {
		return this.payYm;
	}

	public void setPayYm(String payYm) {
		this.payYm = payYm;
	}

	public String getSplrCd() {
		return this.splrCd;
	}

	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUse() {
		return this.use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public Date getUseDt() {
		return this.useDt;
	}

	public void setUseDt(Date useDt) {
		this.useDt = useDt;
	}

	public String getUseDtlItm1() {
		return this.useDtlItm1;
	}

	public void setUseDtlItm1(String useDtlItm1) {
		this.useDtlItm1 = useDtlItm1;
	}

	public String getUseDtlItm10() {
		return this.useDtlItm10;
	}

	public void setUseDtlItm10(String useDtlItm10) {
		this.useDtlItm10 = useDtlItm10;
	}

	public String getUseDtlItm11() {
		return this.useDtlItm11;
	}

	public void setUseDtlItm11(String useDtlItm11) {
		this.useDtlItm11 = useDtlItm11;
	}

	public String getUseDtlItm12() {
		return this.useDtlItm12;
	}

	public void setUseDtlItm12(String useDtlItm12) {
		this.useDtlItm12 = useDtlItm12;
	}

	public String getUseDtlItm13() {
		return this.useDtlItm13;
	}

	public void setUseDtlItm13(String useDtlItm13) {
		this.useDtlItm13 = useDtlItm13;
	}

	public String getUseDtlItm14() {
		return this.useDtlItm14;
	}

	public void setUseDtlItm14(String useDtlItm14) {
		this.useDtlItm14 = useDtlItm14;
	}

	public String getUseDtlItm15() {
		return this.useDtlItm15;
	}

	public void setUseDtlItm15(String useDtlItm15) {
		this.useDtlItm15 = useDtlItm15;
	}

	public String getUseDtlItm16() {
		return this.useDtlItm16;
	}

	public void setUseDtlItm16(String useDtlItm16) {
		this.useDtlItm16 = useDtlItm16;
	}

	public String getUseDtlItm17() {
		return this.useDtlItm17;
	}

	public void setUseDtlItm17(String useDtlItm17) {
		this.useDtlItm17 = useDtlItm17;
	}

	public String getUseDtlItm18() {
		return this.useDtlItm18;
	}

	public void setUseDtlItm18(String useDtlItm18) {
		this.useDtlItm18 = useDtlItm18;
	}

	public String getUseDtlItm19() {
		return this.useDtlItm19;
	}

	public void setUseDtlItm19(String useDtlItm19) {
		this.useDtlItm19 = useDtlItm19;
	}

	public String getUseDtlItm2() {
		return this.useDtlItm2;
	}

	public void setUseDtlItm2(String useDtlItm2) {
		this.useDtlItm2 = useDtlItm2;
	}

	public String getUseDtlItm20() {
		return this.useDtlItm20;
	}

	public void setUseDtlItm20(String useDtlItm20) {
		this.useDtlItm20 = useDtlItm20;
	}

	public String getUseDtlItm21() {
		return this.useDtlItm21;
	}

	public void setUseDtlItm21(String useDtlItm21) {
		this.useDtlItm21 = useDtlItm21;
	}

	public String getUseDtlItm22() {
		return this.useDtlItm22;
	}

	public void setUseDtlItm22(String useDtlItm22) {
		this.useDtlItm22 = useDtlItm22;
	}

	public String getUseDtlItm23() {
		return this.useDtlItm23;
	}

	public void setUseDtlItm23(String useDtlItm23) {
		this.useDtlItm23 = useDtlItm23;
	}

	public String getUseDtlItm24() {
		return this.useDtlItm24;
	}

	public void setUseDtlItm24(String useDtlItm24) {
		this.useDtlItm24 = useDtlItm24;
	}

	public String getUseDtlItm25() {
		return this.useDtlItm25;
	}

	public void setUseDtlItm25(String useDtlItm25) {
		this.useDtlItm25 = useDtlItm25;
	}

	public String getUseDtlItm26() {
		return this.useDtlItm26;
	}

	public void setUseDtlItm26(String useDtlItm26) {
		this.useDtlItm26 = useDtlItm26;
	}

	public String getUseDtlItm3() {
		return this.useDtlItm3;
	}

	public void setUseDtlItm3(String useDtlItm3) {
		this.useDtlItm3 = useDtlItm3;
	}

	public String getUseDtlItm4() {
		return this.useDtlItm4;
	}

	public void setUseDtlItm4(String useDtlItm4) {
		this.useDtlItm4 = useDtlItm4;
	}

	public String getUseDtlItm5() {
		return this.useDtlItm5;
	}

	public void setUseDtlItm5(String useDtlItm5) {
		this.useDtlItm5 = useDtlItm5;
	}

	public String getUseDtlItm6() {
		return this.useDtlItm6;
	}

	public void setUseDtlItm6(String useDtlItm6) {
		this.useDtlItm6 = useDtlItm6;
	}

	public String getUseDtlItm7() {
		return this.useDtlItm7;
	}

	public void setUseDtlItm7(String useDtlItm7) {
		this.useDtlItm7 = useDtlItm7;
	}

	public String getUseDtlItm8() {
		return this.useDtlItm8;
	}

	public void setUseDtlItm8(String useDtlItm8) {
		this.useDtlItm8 = useDtlItm8;
	}

	public String getUseDtlItm9() {
		return this.useDtlItm9;
	}

	public void setUseDtlItm9(String useDtlItm9) {
		this.useDtlItm9 = useDtlItm9;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public String getUsrCd() {
		return this.usrCd;
	}

	public void setUsrCd(String usrCd) {
		this.usrCd = usrCd;
	}

}