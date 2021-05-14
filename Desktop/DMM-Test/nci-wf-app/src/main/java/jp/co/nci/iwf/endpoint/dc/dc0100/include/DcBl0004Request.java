package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書属性ブロック：リクエスト
 */
public class DcBl0004Request extends BaseRequest {

	/** 企業コード */
	public String corporationCode;
	/** 文書フォルダID */
	public Long docFolderId;

}
