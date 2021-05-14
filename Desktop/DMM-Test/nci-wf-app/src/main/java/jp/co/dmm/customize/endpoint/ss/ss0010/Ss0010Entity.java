package jp.co.dmm.customize.endpoint.ss.ss0010;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * SS連携データEntity
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010Entity extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 送信No */
	@Column(name="SND_NO")
	public String sndNo;
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

}
