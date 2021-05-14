package jp.co.dmm.customize.endpoint.mg.mg0280;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メディアマスタ検索リクエスト
 *
 */
public class Mg0280SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** メディアID */
	public String mdaId;
	/** メディア名称 */
	public String mdaNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
