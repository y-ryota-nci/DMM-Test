package jp.co.dmm.customize.endpoint.mg.mg0100;

import java.io.Serializable;
import java.util.Date;

/**
 * 銀行マスタ一覧の検索結果エンティティ
 */
public class Mg0100Entity implements Serializable {

	/** 会社コード */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名 */
	public String bnkNm;
	/** 銀行名カナ*/
	public String bnkNmKn;
	/** 銀行略称 */
	public String bnkNmS;
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
