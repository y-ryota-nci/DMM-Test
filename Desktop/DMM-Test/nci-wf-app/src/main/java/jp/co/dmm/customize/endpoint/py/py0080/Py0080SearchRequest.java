package jp.co.dmm.customize.endpoint.py.py0080;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 前払残高一覧の検索リクエスト
 */
public class Py0080SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 前払金No */
	public String advpayNo;
	/** 前払件名 */
	public String payNm;
	/** 取引先コード */
	public String splrCd;
	public String splrNmKj;
	public String splrNmKn;
	/** ステータス：未消込 */
	public boolean advpaySts0;
	/** ステータス：消込中 */
	public boolean advpaySts1;
	/** ステータス：消込済 */
	public boolean advpaySts2;

}
