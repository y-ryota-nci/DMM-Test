package jp.co.nci.iwf.endpoint.mm.mm0001;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 企業編集画面の更新リクエスト
 */
public class Mm0001UpdateRequest extends BaseRequest {

	public WfmCorporation corp;
}
