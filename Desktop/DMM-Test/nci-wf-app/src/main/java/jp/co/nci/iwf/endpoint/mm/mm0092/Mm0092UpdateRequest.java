package jp.co.nci.iwf.endpoint.mm.mm0092;

import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルックアップグループ設定の更新リクエスト
 */
public class Mm0092UpdateRequest extends BaseRequest {
	public WfmLookupType lookupType;
}
