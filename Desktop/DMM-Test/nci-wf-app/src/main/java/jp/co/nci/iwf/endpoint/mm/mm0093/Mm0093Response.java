package jp.co.nci.iwf.endpoint.mm.mm0093;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ルックアップ設定レスポンス
 */
public class Mm0093Response extends BaseResponse {
	public WfmLookupType lookupType;
	public WfmLookup lookup;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;
	public List<OptionItem> updateFlagList;
}
