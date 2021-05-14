package jp.co.dmm.customize.endpoint.mg.mg0091;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0091UpdateRequest extends BaseRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行口座コード */
	public String bnkaccCd;
	/** 連番 */
	public String sqno;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行口座種別 */
	public String bnkaccTp;
	/** 銀行口座番号 */
	public String bnkaccNo;
	/** 銀行口座名称 */
	public String bnkaccNm;
	/** 銀行口座名称(カタカナ) */
	public String bnkaccNmKn;
	/** 有効期間（開始） */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効期間（終了） */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
