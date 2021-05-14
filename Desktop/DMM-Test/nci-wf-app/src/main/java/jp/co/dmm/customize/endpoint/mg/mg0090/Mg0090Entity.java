package jp.co.dmm.customize.endpoint.mg.mg0090;

import java.io.Serializable;
import java.util.Date;

/**
 * 銀行口座マスタ一覧の検索結果エンティティ
 */

public class Mg0090Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 銀行口座コード */
	public String bnkaccCd;
	/** 連番 */
	public Long sqno;

	public String bnkCd;

	public String bnkaccNm;

	public String bnkaccNmKn;

	public String bnkaccNo;

	public String bnkaccTp;

	public String bnkbrcCd;

	public String dltFg;

	public Date vdDtE;

	public Date vdDtS;

	/** 銀行名称 */
	public String bnkNm;
	/** 銀行支店名称 */
	public String bnkbrcNm;
	/** 銀行口座区分名称 */
	public String bnkaccTpNm;
	/** 削除フラグ名称 */
	public String dltFgNm;

	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
