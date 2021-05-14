package jp.co.dmm.customize.endpoint.py.py0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 変更申請バリデーションのリクエスト
 */
public class Py0030ValidRequest extends BaseRequest {
	/** 会社コード */
	public String companyCd;
	/** 支払No */
	public String payNo;
}
