package jp.co.nci.iwf.endpoint.mm.mm0411;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクション登録リクエスト
 */
public class Mm0411InsertRequest extends BaseRequest {
	public WfmCorporation corporation;
}
