package jp.co.dmm.customize.endpoint.mg.mg0290;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 結合フロアマスタ検索リクエスト
 *
 */
public class Mg0290SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 結合フロアコード */
	public String bndFlrCd;
	/** 結合フロア名称 */
	public String bndFlrNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
