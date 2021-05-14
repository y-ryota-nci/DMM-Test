package jp.co.dmm.customize.endpoint.mg.mg0311;

import java.math.BigDecimal;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 住所マスタ更新リクエスト
 *
 */
public class Mg0311UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 郵便番号 */
	public String zipCd;
	/** 連番 */
	public BigDecimal sqno;
	/** 都道府県コード */
	public String adrPrfCd;
	/** 都道府県 */
	public String adrPrf;
	/** 都道府県カナ */
	public String adrPrfKn;
	/** 市区町村 */
	public String adr1;
	/** 市区町村カナ */
	public String adr1Kn;
	/** 町名地番 */
	public String adr2;
	/** 町名地番カナ */
	public String adr2Kn;
	/** 削除フラグ */
	public String dltFg;
}
