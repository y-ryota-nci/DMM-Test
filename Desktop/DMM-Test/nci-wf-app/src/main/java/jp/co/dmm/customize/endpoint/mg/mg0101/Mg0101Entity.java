package jp.co.dmm.customize.endpoint.mg.mg0101;

import java.io.Serializable;
import java.util.Date;

/**
 * 銀行マスタ編集画面エンティティ
 */
public class Mg0101Entity implements Serializable {

	/** 会社コード */
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
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
