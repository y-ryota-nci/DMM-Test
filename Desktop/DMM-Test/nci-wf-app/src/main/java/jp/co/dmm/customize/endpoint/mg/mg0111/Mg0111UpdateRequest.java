package jp.co.dmm.customize.endpoint.mg.mg0111;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0111UpdateRequest extends BaseRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行支店名称 */
	public String bnkbrcNm;
	/** 銀行支店略称 */
	public String bnkbrcNmS;
	/** 銀行支店名称（カタカナ） */
	public String bnkbrcNmKn;
	/** 有効期間（開始） */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効期間（終了） */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
