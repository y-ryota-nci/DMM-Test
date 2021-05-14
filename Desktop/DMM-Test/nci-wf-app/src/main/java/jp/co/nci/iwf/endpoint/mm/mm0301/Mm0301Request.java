package jp.co.nci.iwf.endpoint.mm.mm0301;

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルート作成リクエスト
 */
public class Mm0301Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmProcessDef processDef;
	public String sourceRoute;
}
