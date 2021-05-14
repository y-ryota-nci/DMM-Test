package jp.co.dmm.customize.endpoint.mg.mg0320;

/**
 * 消費税関連マスタ一覧の検索結果エンティティ
 */

public class Mg0320Entity{

	/** 会社コード */
	public String companyCd;
	/** 消費税種類コード */
	public String taxKndCd;
	public String taxKndNm;

	/** 消費税種別 */
	public String taxSpc;
	public String taxSpcNm;

	/** 消費税コード */
	public String taxCd;
	/** 消費税名称 */
	public String taxNm;

	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
}
