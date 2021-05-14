package jp.co.dmm.customize.endpoint.ri.ri0030;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 検収一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ri0030Entity extends BaseJpaEntity {
		/** 検収No(RCVINSP_NO) */
		@Id
		@Column(name="RCVINSP_NO")
		public String rcvinspNo;
		/** 会社コード(COMPANY_CD) */
		@Column(name="COMPANY_CD")
		public String companyCd;
		/** 検収ステータス(RCVINSP_STS) */
		@Column(name="RCVINSP_STS")
		public String rcvinspSts;
		/** 検収ステータス(RCVINSP_STS_NM) */
		@Column(name="RCVINSP_STS_NM")
		public String rcvinspStsNm;
		/** 検収件名(RCVINSP_NM) */
		@Column(name="RCVINSP_NM")
		public String rcvinspNm;
		/** 取引先コード(SPLR_CD) */
		@Column(name="SPLR_CD")
		public String splrCd;
		/** 取引先名（漢字）(SPLR_NM_KJ) */
		@Column(name="SPLR_NM_KJ")
		public String splrNmKj;
		/** 取引先名（カタカナ）(SPLR_NM_KN) */
		@Column(name="SPLR_NM_KN")
		public String splrNmKn;
		/** 検収金額（税抜）(RCVINSP_AMT_EXCTAX) */
		@Column(name="RCVINSP_AMT_EXCTAX")
		public BigDecimal rcvinspAmtExctax;
		/** 検収金額（税込）(RCVINSP_AMT_INCTAX) */
		@Column(name="RCVINSP_AMT_INCTAX")
		public BigDecimal rcvinspAmtInctax;
		/** 納品日(DLV_DT) */
		@Column(name="DLV_DT")
		public Date dlvDt;
		/** 検収日(RCVINSP_DT) */
		@Column(name="RCVINSP_DT")
		public Date rcvinspDt;
		/** 通貨区分(MNY_TP) */
		@Column(name="MNY_TP")
		public String mnyTp;
		/** 通貨コード(MNY_CD) */
		@Column(name="MNY_CD")
		public String mnyCd;
		/** 通貨区分名称(MNY_NM) */
		@Column(name="MNY_NM")
		public String mnyNm;
}
