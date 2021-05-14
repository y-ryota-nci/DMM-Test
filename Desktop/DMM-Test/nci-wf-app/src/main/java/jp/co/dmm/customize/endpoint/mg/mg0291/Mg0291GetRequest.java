package jp.co.dmm.customize.endpoint.mg.mg0291;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 結合フロアマスタ取得リクエスト
 *
 */
public class Mg0291GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 結合フロアコード */
	public String bndFlrCd;
}
