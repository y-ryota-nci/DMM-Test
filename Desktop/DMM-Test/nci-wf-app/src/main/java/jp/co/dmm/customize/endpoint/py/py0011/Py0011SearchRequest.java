package jp.co.dmm.customize.endpoint.py.py0011;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * クレカ明細消込画面の検索リクエスト
 */
public class Py0011SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 支払月 */
	public String payYm;
	/** 取引先 */
	public String splrNmKj;
	/** ユーザ */
	public String usrNm;
	/** 利用日From */
	public Date useDtFrom;
	/** 利用日To */
	public Date useDtTo;
	/** ステータス：未消込 */
	public boolean matSts0;
	/** ステータス：消込済 */
	public boolean matSts1;

}
