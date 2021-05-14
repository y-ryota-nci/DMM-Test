package jp.co.dmm.customize.endpoint.mg.mg0231;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 支払条件マスタ更新リクエスト
 *
 */
public class Mg0231UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払条件コード */
	public String payCondCd;
	/** 支払条件名称 */
	public String payCondNm;
	/** 削除フラグ */
	public String dltFg;
}
