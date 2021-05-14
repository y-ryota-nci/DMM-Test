package jp.co.dmm.customize.endpoint.py.py0100;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 支払依頼対象選択画面の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0100Entity extends BaseJpaEntity implements Serializable {

	@Column(name="COMPANY_CD")
	public String companyCd;
	@Column(name="RCVINSP_NM")
	public String rcvinspNm;
	@Column(name="PURORD_TP")
	public String purordTp;
	@Column(name="PURORD_NM")
	public String purordNm;
	@Column(name="CNTRCT_NO")
	public String cntrctNo;
	@Column(name="RCVINSP_DT")
	public Date rcvinspDt;
	@Column(name="RCVINSP_YM")
	public String rcvinspYm;
	@Column(name="CST_ADD_YM")
	public String cstAddYm;
	@Column(name="RCVINSP_YM_S")
	public String rcvinspYmS;
	@Column(name="RCVINSP_YM_E")
	public String rcvinspYmE;
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;
	@Column(name="SPLR_CD")
	public String splrCd;
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	@Column(name="PAY_MTH")
	public String payMth;
	@Column(name="PAYEE_BNKACC_CD")
	public String payeeBnkaccCd;
	@Column(name="BNKACC_NM")
	public String bnkaccNm;
	@Column(name="BNK_CD")
	public String bnkCd;
	@Column(name="BNK_NM")
	public String bnkNm;
	@Column(name="BNKBRC_CD")
	public String bnkbrcCd;
	@Column(name="BNKBRC_NM")
	public String bnkbrcNm;
	@Column(name="BNKACC_TP")
	public String bnkaccTp;
	@Column(name="BNKACC_TP_NM")
	public String bnkaccTpNm;
	@Column(name="BNKACC_NO")
	public String bnkaccNo;
	@Column(name="BNKACC_NM_KN")
	public String bnkaccNmKn;
	@Column(name="PAY_CMM_OBL_TP")
	public String payCmmOblTp;
	@Column(name="HLD_TRT_TP")
	public String hldTrtTp;
	@Column(name="SRC_BNKACC_CD")
	public String srcBnkaccCd;
	@Column(name="SRC_BNKACC_NM")
	public String srcBnkaccNm;
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	@Column(name="MNY_CD")
	public String mnyCd;
	@Column(name="MNY_NM")
	public String mnyNm;
	@Column(name="MNY_TP")
	public String mnyTp;
	@Column(name="ADD_RTO")
	public String addRto;
	@Column(name="RDXPNT_GDT")
	public BigDecimal rdxpntGdt;
	@Column(name="PRD_PURORD_TP")
	public String prdPurordTp;
	@Column(name="PRD_PURORD_NO")
	public String prdPurordNo;
	@Column(name="RTN_PAY_NO")
	public String rtnPayNo;
	@Column(name="LOT_NO")
	public String lotNo;
	@Column(name="PAY_SITE_CD")
	public String paySiteCd;
	@Column(name="PAY_SITE_NM")
	public String paySiteNm;
	@Column(name="PAY_APPL_NM")
	public String payApplNm;
	@Column(name="PAY_APPL_CD")
	public String payApplCd;
	@Column(name="ADVPAY_TP")
	public String advpayTp;
	@Column(name="CHRG_BNKACC_CD")
	public String chrgBnkaccCd;
	@Column(name="CHRG_BNKACC_NM")
	public String chrgBnkaccNm;
	@Column(name="PAY_APPL_TP")
	public String payApplTp;
	@Column(name="TAX_UNT")
	public String taxUnt;
	@Column(name="HLDTAX_TP")
	public String hldtaxTp;
	@Column(name="HLDTAX_FG")
	public String hldtaxFg;
	@Column(name="HLDTAX_RTO")
	public BigDecimal hldtaxRto;
	@Column(name="HLDTAX_RTO2")
	public BigDecimal hldtaxRto2;
	@Column(name="HLDTAX_SBJ_AMT")
	public BigDecimal hldtaxSbjAmt;
	@Column(name="HLDTAX_AMT")
	public BigDecimal hldtaxAmt;
	@Column(name="BKBND_CHG_TP")
	public String bkbndChgTp;
	@Column(name="MAIL_TP")
	public String mailTp;
	@Column(name="MAIL_RMK")
	public String mailRmk;
	@Column(name="SHT_TO_ZIP_CD")
	public String shtToZipCd;
	@Column(name="SHT_ADR_PRF_CD")
	public String shtAdrPrfCd;
	@Column(name="SHT_ADR_PRF")
	public String shtAdrPrf;
	@Column(name="SHT_ADR1")
	public String shtAdr1;
	@Column(name="SHT_ADR2")
	public String shtAdr2;
	@Column(name="SHT_ADR3")
	public String shtAdr3;
	@Column(name="SHT_PIC_NM")
	public String shtPicNm;
	@Column(name="SHT_DPT_NM")
	public String shtDptNm;
	@Column(name="SHT_MLADR")
	public String shtMladr;
	@Column(name="SHT_TEL_NO")
	public String shtTelNo;
	@Column(name="ADVCST_MRK")
	public String advcstMrk;
	@Column(name="RCVINSP_NO")
	public String rcvinspNo;
	@Column(name="RCVINSP_DTL_NO")
	public BigDecimal rcvinspDtlNo;
	@Id
	@Column(name="DISP_RCVINSP_NO")
	public String dispRcvinspNo;
	@Column(name="PURORD_NO")
	public String purordNo;
	@Column(name="PURORD_DTL_NO")
	public BigDecimal purordDtlNo;
	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;
	@Column(name="ITMEXPS_NM1")
	public String itmexpsNm1;
	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;
	@Column(name="ITMEXPS_NM2")
	public String itmexpsNm2;
	@Column(name="SLP_GRP_GL")
	public String slpGrpGl;
	@Column(name="CST_TP")
	public String cstTp;
	@Column(name="ITM_CD")
	public String itmCd;
	@Column(name="ITM_NM")
	public String itmNm;
	@Column(name="IPT_NM_FG")
	public String iptNmFg;
	@Column(name="BUMON_CD")
	public String bumonCd;
	@Column(name="BUMON_NM")
	public String bumonNm;
	@Column(name="ANLYS_CD")
	public String anlysCd;
	@Column(name="ASST_TP")
	public String asstTp;
	@Column(name="RCVINSP_AMT")
	public BigDecimal rcvinspAmt;
	@Column(name="RCVINSP_AMT_INCTAX")
	public BigDecimal rcvinspAmtInctax;
	@Column(name="ADVPAY_APLY_AMT")
	public BigDecimal advpayAplyAmt;
	@Column(name="RCVINSP_SMRY")
	public String rcvinspSmry;
	@Column(name="TAX_FG")
	public String taxFg;
	@Column(name="TAX_CD")
	public String taxCd;
	@Column(name="TAX_TP")
	public String taxTp;
	@Column(name="TAX_RTO")
	public BigDecimal taxRto;
	@Column(name="APPL_CONT")
	public String applCont;
	@Column(name="CTRCT_TP")
	public String ctrctTp;
	@Column(name="BRKDWN_TP")
	public String brkdwnTp;
	@Column(name="APPL_PRD_S_DT")
	public Date applPrdSDt;
	@Column(name="APPL_PRD_E_DT")
	public Date applPrdEDt;
	@Column(name="ADVPAY_NO")
	public String advpayNo;
	@Column(name="INV_COMPANY_CD")
	public String invCompanyCd;
	@Column(name="DVC_CD")
	public String dvcCd;
	@Column(name="BND_FLR_CD")
	public String bndFlrCd;
	@Column(name="BND_FLR_NM")
	public String bndFlrNm;
	@Column(name="FRT_PNT_CD")
	public String frtPntCd;
	@Column(name="MSR_TOOL_CD")
	public String msrToolCd;
	@Column(name="MDA_ID")
	public String mdaId;
	@Column(name="ADVPAY_ADD_RTO")
	public String advpayAddRto;
	@Column(name="ADVPAY_PAY_PLN_DT")
	public Date advpayPayPlnDt;
	@Column(name="TAX_SBJ_TP")
	public String taxSbjTp;
	@Column(name="TAX_KND_CD")
	public String taxKndCd;
	@Column(name="TAX_FG_CHG")
	public String taxFgChg;

}
