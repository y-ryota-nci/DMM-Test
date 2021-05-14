package jp.co.dmm.customize.endpoint.mg.mg0241;

import java.math.BigDecimal;

/**
 * 支払サイトマスタ編集画面エンティティ
 */

public class Mg0241Entity {

	/** 会社コード */
	public String companyCd;
	/** 支払サイトコード */
	public String paySiteCd;
	/** 支払サイト名称 */
	public String paySiteNm;
	/** 支払サイト（月） */
	public String paySiteM;
	/** 支払サイト（日） */
	public String paySiteN;
	/** 削除フラグ */
	public String dltFg;
	/** ソート順 */
	public BigDecimal sortOrder;
}
