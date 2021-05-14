package jp.co.dmm.customize.endpoint.ri.ri0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 検収＿変更申請用バリデーションリクエスト
 */
public class Ri0030ValidateRequest extends BaseRequest {
	/** 会社CD */
	public String companyCd;
	/** 検収No */
	public String rcvinspNo;
}
