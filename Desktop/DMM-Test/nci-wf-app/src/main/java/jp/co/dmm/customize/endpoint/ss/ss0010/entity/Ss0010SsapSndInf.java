package jp.co.dmm.customize.endpoint.ss.ss0010.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * SS-AP_送信情報
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010SsapSndInf extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** SSAP送信No */
	@Column(name="SSAP_SND_NO")
	public String ssapSndNo;
	/** キャンセル区分 */
	@Column(name="CNCL_TP")
	public String cnclTp;
	/** 作成システム */
	@Column(name="MAK_SYS")
	public String makSys;
	/** 作成日 */
	@Column(name="MAK_DT")
	public Date makDt;
	/** 作成時間 */
	@Column(name="MAK_TM")
	public String makTm;
	/** 送信状況 */
	@Column(name="SND_STS")
	public String sndSts;
	/** 送信状況名称 */
	@Column(name="SND_STS_NM")
	public String sndStsNm;
	/** 送信日 */
	@Column(name="SND_DT")
	public Date sndDt;
	/** 送信時間 */
	@Column(name="SND_TM")
	public String sndTm;
	/** 支払履歴No */
	@Column(name="PAYHYS_NO")
	public String payhysNo;
	/** 仕訳伝票No */
	@Column(name="JRNSLP_NO")
	public String jrnslpNo;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
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
