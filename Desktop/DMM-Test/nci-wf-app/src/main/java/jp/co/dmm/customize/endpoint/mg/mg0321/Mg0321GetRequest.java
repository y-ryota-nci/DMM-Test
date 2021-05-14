package jp.co.dmm.customize.endpoint.mg.mg0321;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税関連マスタ取得リクエスト
 *
 */
public class Mg0321GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税種類コード */
	public String taxKndCd;
	/** 消費税種別 */
	public String taxSpc;
}
