package jp.co.dmm.customize.endpoint.mg.mg0290;

import java.math.BigDecimal;

/**
 * 結合フロアマスタ一覧の検索結果エンティティ
 */

public class Mg0290Entity{

	/** 会社コード */
	public String companyCd;
	/** 結合フロアコード */
	public String bndFlrCd;
	/** 結合フロア名称 */
	public String bndFlrNm;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
}
