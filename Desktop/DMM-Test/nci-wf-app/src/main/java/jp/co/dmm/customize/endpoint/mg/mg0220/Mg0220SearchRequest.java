package jp.co.dmm.customize.endpoint.mg.mg0220;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払業務マスタ検索リクエスト
 *
 */
public class Mg0220SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払業務コード */
	public String payApplCd;
	/** 支払業務名称 */
	public String payApplNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
