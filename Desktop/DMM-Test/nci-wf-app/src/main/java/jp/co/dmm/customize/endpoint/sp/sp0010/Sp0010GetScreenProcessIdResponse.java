package jp.co.dmm.customize.endpoint.sp.sp0010;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面プロセスID取得結果レスポンス
 */
public class Sp0010GetScreenProcessIdResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 画面プロセスID */
	public Integer screenProcessId;
	/** 会社コード */
	public String companyCd;
}
