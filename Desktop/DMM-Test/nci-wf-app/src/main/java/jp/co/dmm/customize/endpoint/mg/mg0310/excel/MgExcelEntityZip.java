package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityZip extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 郵便番号 */
	@Column(name="ZIP_CD")
	public String zipCd;
	/** 連番 */
	@Column(name="SQNO")
	public String sqno;

	/** 住所（都道府県）コード */
	@Column(name="ADR_PRF_CD")
	public String adrPrfCd;
	/** 住所（都道府県） */
	@Column(name="ADR_PRF")
	public String adrPrf;
	/** 住所（都道府県）（カタカナ） */
	@Column(name="ADR_PRF_KN")
	public String adrPrfKn;

	/** 住所（市区町村） */
	@Column(name="ADR1")
	public String adr1;
	/** 住所（市区町村）（カタカナ） */
	@Column(name="ADR1_KN")
	public String adr1Kn;

	/** 住所（町名番地） */
	@Column(name="ADR2")
	public String adr2;
	/** 住所（町名番地）（カタカナ） */
	@Column(name="ADR2_KN")
	public String adr2Kn;


	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
}
