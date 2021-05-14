package jp.co.nci.iwf.endpoint.mm.mm0100;

import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 情報共有設定の検索レスポンス
 */
public class Mm0100SearchResponse extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public Map<String, WfmInformationSharerDef> informationSharerDefs;
}