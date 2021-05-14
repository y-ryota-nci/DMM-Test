package jp.co.nci.iwf.endpoint.mm.mm0081;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション設定レスポンス
 */
public class Mm0081Response extends BaseResponse {
	public WfmFunction function;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** 同期の選択肢 */
	public List<OptionItem> synchronousFunctionFlagList;

}
