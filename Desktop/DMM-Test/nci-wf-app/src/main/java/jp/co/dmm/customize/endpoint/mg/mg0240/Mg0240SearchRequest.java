package jp.co.dmm.customize.endpoint.mg.mg0240;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払サイトマスタ検索リクエスト
 *
 */
public class Mg0240SearchRequest extends BasePagingRequest {

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
	public boolean dltFgOff;
	public boolean dltFgOn;
}
