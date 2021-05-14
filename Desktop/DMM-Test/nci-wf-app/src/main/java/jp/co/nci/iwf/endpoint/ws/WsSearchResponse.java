package jp.co.nci.iwf.endpoint.ws;

import jp.co.nci.integrated_workflow.model.base.WfmAuthTransfer;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 代理設定一覧のレスポンス.
 */
public class WsSearchResponse extends BasePagingResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** 代理設定情報 */
	public WfmAuthTransfer wfmAuthTransfer;

}