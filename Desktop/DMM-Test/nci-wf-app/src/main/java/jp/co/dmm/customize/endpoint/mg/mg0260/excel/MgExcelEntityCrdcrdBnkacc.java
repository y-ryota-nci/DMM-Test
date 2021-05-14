package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityCrdcrdBnkacc extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名称（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** ユーザコード */
	@Column(name="USR_CD")
	public String usrCd;
	@Column(name="USR_NM")
	public String usrNm;
	/** カード会社名称 */
	@Column(name="CRD_COMPANY_NM")
	public String crdCompanyNm;
	/** 口座コード */
	@Column(name="BNKACC_CD")
	public String bnkaccCd;
	/** 口座引落日 */
	@Column(name="BNKACC_CHRG_DT")
	public String bnkaccChrgDt;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
