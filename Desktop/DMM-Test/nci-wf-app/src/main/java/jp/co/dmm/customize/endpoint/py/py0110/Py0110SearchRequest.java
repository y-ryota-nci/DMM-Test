package jp.co.dmm.customize.endpoint.py.py0110;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 前払選択の検索リクエスト
 */
public class Py0110SearchRequest extends BasePagingRequest {
	/** 前払No */
	public String maebaraiNo;
	/** 前払件名 */
	public String maebaraiNm;
	/** 取引先コード */
	public String splrCd;
	public String splrNmKj;
	public String splrNmKn;
}
