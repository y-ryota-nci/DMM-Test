package jp.co.dmm.customize.endpoint.ri.ri0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 変更申請バリデーションのリクエスト
 */
public class Ri0030ValidRequest extends BaseRequest {
	/** 会社コード */
	public String companyCd;
	/** 検収No */
	public String rcvinspNo;
}
