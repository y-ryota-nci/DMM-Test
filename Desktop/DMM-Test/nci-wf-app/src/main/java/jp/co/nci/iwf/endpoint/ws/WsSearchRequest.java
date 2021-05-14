package jp.co.nci.iwf.endpoint.ws;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 代理設定一覧のリクエスト.
 */
public class WsSearchRequest extends BasePagingRequest {

	/** */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String corporationCode;
	/** 権限委譲連番 */
	public Long seqNoAuthTransfer;
	/** 代理元ユーザコード */
	public String userCode;

	/** 削除一覧 */
	public List<String> deleteList;

}