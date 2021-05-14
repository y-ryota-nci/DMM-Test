package jp.co.dmm.customize.endpoint.mg.mg0021;

import java.io.Serializable;

/**
 * 費目マスタ設定画面エンティティ
 */

public class Mg0021Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 費目コード */
	public String itmexpsCd;
	/** 費目名称 */
	public String itmexpsNm;
	/** 費目略称 */
	public String itmexpsNmS;
	/** 費目階層 */
	public Long itmexpsLevel;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
}
