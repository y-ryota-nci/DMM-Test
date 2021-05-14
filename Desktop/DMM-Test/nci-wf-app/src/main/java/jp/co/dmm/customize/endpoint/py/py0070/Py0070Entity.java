package jp.co.dmm.customize.endpoint.py.py0070;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 買掛残高の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Py0070Entity extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 会社名 */
	@Column(name="COMPANY_NM")
	public String companyNm;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名(漢字) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 期間(From) */
	@Column(name="ADD_YM_S")
	public String addYmS;
	/** 期間(To) */
	@Column(name="ADD_YM_E")
	public String addYmE;
	/** 勘定科目コード */
	@Column(name="ACC_CD")
	public String accCd;
	/** 勘定科目名称 */
	@Column(name="ACC_NM")
	public String accNm;
	/** 前月残高邦貨金額 */
	@Column(name="PRV_BAL_AMT_JPY")
	public BigDecimal prvBalAmtJpy;
	/** 借方邦貨金額 */
	@Column(name="DBT_AMT_JPY")
	public BigDecimal dbtAmtJpy;
	/** 貸方邦貨金額 */
	@Column(name="CDT_AMT_JPY")
	public BigDecimal cdtAmtJpy;
	/** 当月残高邦貨金額 */
	@Column(name="NXT_BAL_AMT_JPY")
	public BigDecimal nxtBalAmtJpy;
}
