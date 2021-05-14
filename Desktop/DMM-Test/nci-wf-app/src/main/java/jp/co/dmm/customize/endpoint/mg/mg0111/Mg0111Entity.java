package jp.co.dmm.customize.endpoint.mg.mg0111;

import java.io.Serializable;
import java.util.Date;

/**
 * 銀行支店マスタ設定画面エンティティ
 */
public class Mg0111Entity implements Serializable {

	/** 会社コード */
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
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
