package jp.co.dmm.customize.endpoint.mg.mg0110;

import java.io.Serializable;
import java.util.Date;

/**
 * 銀行支店マスタ一覧の検索結果エンティティ
 */
public class Mg0110Entity implements Serializable {

	/** 会社コード */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名称 */
	public String bnkNm;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行支店名*/
	public String bnkbrcNm;
	/** 銀行支店略称*/
	public String bnkbrcNmS;
	/** 銀行支店名（カナ）*/
	public String bnkbrcNmKn;
	/** 有効期限（開始） */
	public Date vdDtS;
	/** 有効期限（終了） */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;

	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
