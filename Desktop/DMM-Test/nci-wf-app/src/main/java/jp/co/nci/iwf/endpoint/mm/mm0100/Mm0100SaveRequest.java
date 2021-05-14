package jp.co.nci.iwf.endpoint.mm.mm0100;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 情報共有設定の更新・削除リクエスト
 */
public class Mm0100SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmInformationSharerDef informationSharerDef;
	public List<WfmInformationSharerDef> informationSharerDefs;
}
