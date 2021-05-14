package jp.co.nci.iwf.endpoint.mm.mm0091;

import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルックアップグループの登録リクエスト
 */
public class Mm0091InsertRequest extends BaseRequest {
	public WfmLookupType lookupType;
}
