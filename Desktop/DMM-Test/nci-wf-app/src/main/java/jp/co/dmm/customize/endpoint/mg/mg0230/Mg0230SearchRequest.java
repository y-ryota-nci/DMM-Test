package jp.co.dmm.customize.endpoint.mg.mg0230;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払条件マスタ検索リクエスト
 *
 */
public class Mg0230SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払条件コード */
	public String payCondCd;
	/** 支払条件名称 */
	public String payCondNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
