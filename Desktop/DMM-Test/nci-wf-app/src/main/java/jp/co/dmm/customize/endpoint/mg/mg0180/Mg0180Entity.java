package jp.co.dmm.customize.endpoint.mg.mg0180;

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
 * 社内レートマスタ一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mg0180Entity extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 連番 */
	@Column(name="SQNO")
	public long sqno;
	/** 通貨名称 */
	@Column(name="MNY_NM")
	public String mnyNm;
	/** 社内レート */
	@Column(name="IN_RTO")
	public BigDecimal inRto;
	/** レートタイプ */
	@Column(name="RTO_TP")
	public String rtoTp;
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

}
