package jp.co.dmm.customize.endpoint.py.py0110;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 前払選択の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0110Entity extends BaseJpaEntity {
		/** 前払No(MAEBARAI_NO) */
		@Id
		@Column(name="MAEBARAI_NO")
		public String maebaraiNo;
		/** 会社CD(COMPANY_CD) */
		@Column(name="COMPANY_CD")
		public String companyCd;
		/** 前払件名(MAEBARAI_NM) */
		@Column(name="MAEBARAI_NM")
		public String maebaraiNm;
		/** 取引先コード(SPLR_CD) */
		@Column(name="SPLR_CD")
		public String splrCd;
		/** 取引先名（漢字）(SPLR_NM_KJ) */
		@Column(name="SPLR_NM_KJ")
		public String splrNmKj;
		/** 取引先名（カタカナ）(SPLR_NM_KN) */
		@Column(name="SPLR_NM_KN")
		public String splrNmKn;
		/** 通貨(MNY_NM) */
		@Column(name="MNY_NM")
		public String mnyNm;
		/** 前払金額(MAEBARAI_AMT_JPY) */
		@Column(name="MAEBARAI_AMT_JPY")
		public BigDecimal maebaraiAmtJpy;
		/** 充当済金額(JUTO_AMT_JPY) */
		@Column(name="JUTO_AMT_JPY")
		public BigDecimal jutoAmtJpy;
		/** 充当可能金額(JUTO_KANO_AMT_JPY) */
		@Column(name="JUTO_KANO_AMT_JPY")
		public BigDecimal jutoKanoAmtJpy;
		/** 今回充当金額(JUTO_NOW_AMT_JPY) */
		@Column(name="JUTO_NOW_AMT_JPY")
		public BigDecimal jutoNowAmtJpy;
}
