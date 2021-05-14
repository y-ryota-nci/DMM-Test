package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBumon extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 部門名称 */
	@Column(name="BUMON_NM")
	public String bumonNm;
	/** 消費税種類コード */
	@Column(name="TAX_KND_CD")
	public String taxKndCd;

	/** 事業分類コード */
	@Column(name="ENTRP_TP_CD")
	public String entrpTpCd;
	/** 事業コード */
	@Column(name="ENTRP_CD")
	public String entrpCd;
	/** タブコード */
	@Column(name="TAB_CD")
	public String tabCd;
	/** サイトコード */
	@Column(name="SITE_CD")
	public String siteCd;
	/** 分類コード */
	@Column(name="TP_CD")
	public String tpCd;
	/** 地域コード */
	@Column(name="AREA_CD")
	public String areaCd;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
