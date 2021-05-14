package jp.co.nci.iwf.endpoint.ws;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 代理設定画面のリクエスト.
 */
public class WsSaveRequest extends BaseRequest {

	/** */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String corporationCode;
	/** 権限移譲連番 */
	public Long seqNoAuthTransfer;

}