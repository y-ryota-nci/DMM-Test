package jp.co.nci.iwf.endpoint.mm.mm0301;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ルート作成レスポンス
 */
public class Mm0301Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** プロセス定義の選択肢 */
	public List<WfmProcessDef> processDefs;
	public List<OptionItem> executionTermUnitTypes;

	public WfmProcessDef processDef;

}