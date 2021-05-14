package jp.co.dmm.customize.endpoint.mg.mg0240;

import java.math.BigDecimal;

/**
 * 支払サイトマスタ一覧の検索結果エンティティ
 */

public class Mg0240Entity{

	/** 会社コード */
	public String companyCd;
	/** 支払サイトコード */
	public String paySiteCd;
	/** 支払サイト名称 */
	public String paySiteNm;
	/** 支払サイト（付数） */
	public String paySiteM;
	/** 支払サイト（日数） */
	public String paySiteN;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
	/** ソート順 */
	public BigDecimal sortOrder;
}
