package jp.co.dmm.customize.endpoint.mg.mg0271;

import java.math.BigDecimal;

/**
 * 予算科目編集画面エンティティ
 */

public class Mg0271Entity{

	/** 会社コード */
	public String companyCd;
	/** 予算科目コード */
	public String bgtItmCd;
	/** 予算科目名称 */
	public String bgtItmNm;
	/** BS/BL区分 */
	public String bsPlTp;
	/** ソート順 */
	public BigDecimal sortOrder;


	/** 削除フラグ */
	public String dltFg;
}
