package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メモ情報ブロック：リクエスト
 */
public class DcBl0008Request extends BaseRequest {

	/** 文書ID */
	public Long docId;
	/** メモ */
	public String memo;

}
