package jp.co.nci.iwf.endpoint.mm.mm0071;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション設定レスポンス
 */
public class Mm0071Response extends BaseResponse {
	public WfmAction action;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** アクション区分の選択肢 */
	public List<OptionItem> actionTypeList;

}
