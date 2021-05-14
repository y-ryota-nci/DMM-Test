package jp.co.dmm.customize.endpoint.mg.mg0230;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 通貨マスタ削除処理リクエスト
 *
 */
public class Mg0230RemoveRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払条件コードコード */
	public String payCondCd;

}
