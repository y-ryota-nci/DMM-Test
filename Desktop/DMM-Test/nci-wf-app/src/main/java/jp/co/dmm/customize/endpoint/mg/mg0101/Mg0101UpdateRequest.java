package jp.co.dmm.customize.endpoint.mg.mg0101;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0101UpdateRequest extends BaseRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名称 */
	public String bnkNm;
	/** 銀行略称 */
	public String bnkNmS;
	/** 銀行名称（カタカナ） */
	public String bnkNmKn;
	/** 有効期間（開始） */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効期間（終了） */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
