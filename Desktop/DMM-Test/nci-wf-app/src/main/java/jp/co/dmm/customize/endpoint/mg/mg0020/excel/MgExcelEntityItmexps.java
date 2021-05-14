package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityItmexps extends MgExcelEntity {
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 費目コード */
	@Column(name="ITMEXPS_CD")
	public String itmexpsCd;
	/** 費目名称 */
	@Column(name="ITMEXPS_NM")
	public String itmexpsNm;
	/** 費目略称 */
	@Column(name="ITMEXPS_NM_S")
	public String itmexpsNmS;
	/** 費目階層 */
	@Column(name="ITMEXPS_LEVEL")
	public String itmexpsLevel;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
