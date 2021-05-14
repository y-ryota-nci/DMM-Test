package jp.co.dmm.customize.endpoint.py.py0030;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払一覧の検索リクエスト
 */
public class Py0030SearchRequest extends BasePagingRequest {
	/** 支払No */
	public String payNo;
	/** 支払件名 */
	public String payNm;
	/** 取引先 */
	public String splrNmKj;
	/** 支払予定日From */
	public Date payPlnDtFrom;
	/** 支払予定日To */
	public Date payPlnDtTo;
}
