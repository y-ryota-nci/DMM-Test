package jp.co.dmm.customize.endpoint.mg.mg0210;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 源泉税区分マスタ一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mg0210Entity extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 会社名称 */
	@Column(name="COMPANY_NM")
	public String companyNm;
	/** 源泉税区分 */
	@Column(name="HLDTAX_TP")
	public String hldtaxTp;
	/** 源泉税名称 */
	@Column(name="HLDTAX_NM")
	public String hldtaxNm;
	/** 源泉税率1 */
	@Column(name="HLDTAX_RTO1")
	public BigDecimal hldtaxRto1;
	/** 源泉税率2 */
	@Column(name="HLDTAX_RTO2")
	public BigDecimal hldtaxRto2;
	/** 有効期間(開始) */
	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効期間(終了) */
	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_E")
	public Date vdDtE;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;
	/** 作成会社コード */
	@Column(name="CORPORATION_CODE_CREATED")
	public String corporationCodeCreated;
	/** 作成ユーザコード */
	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;
	/** 作成ユーザIPアドレス */
	@Column(name="IP_CREATED")
	public String ipCreated;
	/** 作成日時 */
	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;
	/** 更新会社コード */
	@Column(name="CORPORATION_CODE_UPDATED")
	public String corporationCodeUpdated;
	/** 更新ユーザコード */
	@Column(name="USER_CODE_UPDATED")
	public String userCodeUpdated;
	/** 更新ユーザIPアドレス */
	@Column(name="IP_UPDATED")
	public String ipUpdated;
	/** 更新日時 */
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;
	/** ソート順 */
	@Column(name="SORT_ORDER")
	public BigDecimal sortOrder;
	/** 勘定科目コード */
	@Column(name="ACC_CD")
	public String accCd;
	/** 勘定科目名称 */
	@Column(name="ACC_NM")
	public String accNm;
	/** 勘定科目補助コード */
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;
	/** 勘定科目補助名称 */
	@Column(name="ACC_BRKDWN_NM")
	public String accBrkdwnNm;

}
