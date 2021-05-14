package jp.co.dmm.customize.endpoint.mg.mg0231;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払条件マスタ取得リクエスト
 *
 */
public class Mg0231GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払条件コードコード */
	public String payCondCd;
}
