package jp.co.dmm.customize.endpoint.mg.mg0250;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 部門関連マスタ検索リクエスト
 *
 */
public class Mg0250SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 部門コード */
	public String bumonCd;
	public String bumonNm;
	/** 組織コード */
	public String orgnzCd;
	public String orgnzNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
