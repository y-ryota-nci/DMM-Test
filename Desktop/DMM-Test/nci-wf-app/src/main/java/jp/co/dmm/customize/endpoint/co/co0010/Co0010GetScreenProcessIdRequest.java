package jp.co.dmm.customize.endpoint.co.co0010;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面プロセスID取得リクエスト
 */
public class Co0010GetScreenProcessIdRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;

	/** 契約No */
	public String cntrctNo;
}
