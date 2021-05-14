package jp.co.dmm.customize.endpoint.mg.mg0270;

import java.math.BigDecimal;

/**
 * 予算科目マスタ一覧の検索結果エンティティ
 */

public class Mg0270Entity{

	/** 会社コード */
	public String companyCd;
	/** 予算科目コード */
	public String bgtItmCd;
	/** 予算科目名称 */
	public String bgtItmNm;
	/** BS/PL区分 */
	public String bsPlTp;
	/** BS/PL区分名称 */
	public String bsPlTpNm;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
}
