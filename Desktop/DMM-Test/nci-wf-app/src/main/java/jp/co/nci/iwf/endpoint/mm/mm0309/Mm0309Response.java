package jp.co.nci.iwf.endpoint.mm.mm0309;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション機能定義作成レスポンス
 */
public class Mm0309Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<WfmFunction> functions;
	public List<OptionItem> executionTimingTypes;

	public WfmFunctionDef functionDef;

}