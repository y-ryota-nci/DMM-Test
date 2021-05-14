package jp.co.dmm.customize.endpoint.mg.mg0221;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 支払業務マスタ更新リクエスト
 *
 */
public class Mg0221UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払業務コード */
	public String payApplCd;
	/** 支払業務名称 */
	public String payApplNm;
	/** 削除フラグ */
	public String dltFg;
}
