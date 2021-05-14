package jp.co.dmm.customize.endpoint.co;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.SplrMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 管理_契約登録（取引先明細）画面エンティティ
 */
@Entity(name="CoSplrMstEntity")
@Access(AccessType.FIELD)
public class SplrMstEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SplrMstPK id;

	/** 取引先コード */
	@Column(name="SPLR_NO")
	public Long splrNo;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 契約No */
	@Column(name="CNTRCT_NO")
	public String cntrctNo;
	/** 連番 */
	@Column(name="SQNO")
	public Long sqno;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名（カナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

}
