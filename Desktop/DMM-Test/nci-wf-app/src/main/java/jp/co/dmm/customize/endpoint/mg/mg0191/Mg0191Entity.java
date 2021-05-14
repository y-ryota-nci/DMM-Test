package jp.co.dmm.customize.endpoint.mg.mg0191;

import java.io.Serializable;

/**
 * 部門マスタ設定画面エンティティ
 */

public class Mg0191Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 部門コード */
	public String bumonCd;
	/** 部門名称 */
	public String bumonNm;
	/** 事業分類コード */
	public String entrpTpCd;
	/** 事業コード */
	public String entrpCd;
	/** タブコード */
	public String tabCd;
	/** サイトコード */
	public String siteCd;
	/** 分類コード */
	public String tpCd;
	/** 地域コード */
	public String areaCd;
	/** 消費税種類コード */
	public String taxKndCd;
	/** 削除フラグ */
	public String dltFg;

	/** 事業名称 */
	public String entrpNm;
	/** タブ名称 */
	public String tabNm;
	/** サイト名称 */
	public String siteNm;
	/** 分類名称 */
	public String tpNm;
	/** 地域名称 */
	public String areaNm;
	/** 消費税種類名称 */
	public String taxKndNm;
	/** 削除フラグ名称 */
	public String dltFgNm;

}
