package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBumonExps extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 組織コード */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

}
