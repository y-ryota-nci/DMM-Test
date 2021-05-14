package jp.co.dmm.customize.endpoint.mg.mg0310;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 住所マスタ検索リクエスト
 *
 */
public class Mg0310SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社名 */
	public String companyNm;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 郵便番号 */
	public String zipCd;
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
	/** 住所 */
	public String adr;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
