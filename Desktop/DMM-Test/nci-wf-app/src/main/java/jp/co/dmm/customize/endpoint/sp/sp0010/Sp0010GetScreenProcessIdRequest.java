package jp.co.dmm.customize.endpoint.sp.sp0010;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面プロセスID取得リクエスト
 */
public class Sp0010GetScreenProcessIdRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;

	/** 取引先コード */
	public String splrCd;
}
