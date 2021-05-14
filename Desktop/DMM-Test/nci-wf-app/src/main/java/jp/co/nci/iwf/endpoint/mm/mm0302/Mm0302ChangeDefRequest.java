package jp.co.nci.iwf.endpoint.mm.mm0302;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者変更定義リクエスト
 */
public class Mm0302ChangeDefRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmChangeDef changeDef;

}
