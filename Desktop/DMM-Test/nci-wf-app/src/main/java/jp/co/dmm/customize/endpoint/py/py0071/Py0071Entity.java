package jp.co.dmm.customize.endpoint.py.py0071;

import java.math.BigDecimal;
import java.util.Date;

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
public class Py0071Entity extends BaseJpaEntity {

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
	/** 仕訳伝票No */
	@Column(name="JRNSLP_NO")
	public String jrnslpNo;
	/** 仕訳伝票明細No */
	@Column(name="JRNSLP_DTL_NO")
	public long jrnslpDtlNo;
	/** 作成システム */
	@Column(name="MK_SYS")
	public String mkSys;
	/** 仕訳種別 */
	@Column(name="JRN_TP")
	public String jrnTp;
	/** 赤黒区分 */
	@Column(name="RB_TP")
	public String rbTp;
	/** 計上日 */
	@Column(name="ADD_DT")
	public Date addDt;
	/** 貸借区分 */
	@Column(name="DC_TP")
	public String dcTp;
	/** 会計部門コード */
	@Column(name="ACC_DPT_CD")
	public String accDptCd;
	/** 会計部門名称 */
	@Column(name="ACC_DPT_NM")
	public String accDptNm;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名(漢字) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 勘定科目コード */
	@Column(name="ACC_CD")
	public String accCd;
	/** 勘定科目枝番 */
	@Column(name="ACC_X")
	public long accX;
	/** 勘定科目名称 */
	@Column(name="ACC_NM")
	public String accNm;
	/** 勘定科目補助コード */
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;
	/** 勘定科目補助枝番 */
	@Column(name="ACC_BRKDWN_X")
	public long accBrkdwnX;
	/** 勘定科目補助名称 */
	@Column(name="ACC_BRKDWN_NM")
	public String accBrkdwnMm;
	/** 相手勘定科目コード */
	@Column(name="PRTN_ACC_CD")
	public String prtnAccCd;
	/** 相手勘定科目枝番 */
	@Column(name="PRTN_ACC_X")
	public long prtnAccX;
	/** 相手勘定科目名称 */
	@Column(name="PRTN_ACC_NM")
	public String prtnAccNm;
	/** 相手勘定科目補助コード */
	@Column(name="PRTN_ACC_BRKDWN_CD")
	public String prtnAccBrkdwnCd;
	/** 相手勘定科目補助枝番 */
	@Column(name="PRTN_ACC_BRKDWN_X")
	public long prtnAccBrkdwnX;
	/** 相手勘定科目補助名称 */
	@Column(name="PRTN_ACC_BRKDWN_NM")
	public String prtnAccBrkdwnNm;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 借方外貨金額 */
	@Column(name="DBT_AMT_FC")
	public BigDecimal dbtAmtFc;
	/** 借方邦貨金額 */
	@Column(name="DBT_AMT_JPY")
	public BigDecimal dbtAmtJpy;
	/** 貸方外貨金額 */
	@Column(name="CDT_AMT_FC")
	public BigDecimal cdtAmtFc;
	/** 貸方邦貨金額 */
	@Column(name="CDT_AMT_JPY")
	public BigDecimal cdtAmtJpy;
	/** 摘要(1) */
	@Column(name="ABST1")
	public String abst1;
	/** 摘要(2) */
	@Column(name="ABST2")
	public String abst2;
	/** 摘要(1)＆摘要(2) */
	@Column(name="ABST")
	public String abst;
}
