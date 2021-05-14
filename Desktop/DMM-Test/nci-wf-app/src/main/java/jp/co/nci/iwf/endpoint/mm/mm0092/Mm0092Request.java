package jp.co.nci.iwf.endpoint.mm.mm0092;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ルックアップグループ設定リクエスト
 */
public class Mm0092Request extends BasePagingRequest {
	public String corporationCode;
	public String lookupTypeCode;
	public Long timestampUpdated;

	public List<WfmLookup> deleteLookups;
}
