package jp.co.dmm.customize.endpoint.mg.mg0311;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 住所マスタ取得リクエスト
 *
 */
public class Mg0311GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 郵便番号 */
	public String zipCd;
	/** 連番 */
	public String sqno;

}
