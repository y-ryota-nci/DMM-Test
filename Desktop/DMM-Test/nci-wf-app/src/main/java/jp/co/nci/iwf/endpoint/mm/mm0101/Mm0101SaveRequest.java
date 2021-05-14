package jp.co.nci.iwf.endpoint.mm.mm0101;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 情報共有者定義作成の保存リクエスト
 */
public class Mm0101SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<WfmInformationSharerDef> informationSharerDefs;

}
