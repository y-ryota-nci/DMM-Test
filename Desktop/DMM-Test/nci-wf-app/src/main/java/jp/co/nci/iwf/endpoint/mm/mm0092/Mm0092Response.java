package jp.co.nci.iwf.endpoint.mm.mm0092;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ルックアップグループ設定レスポンス
 */
public class Mm0092Response extends BasePagingResponse {
	public WfmLookupType lookupType;

	public List<WfmLookup> lookupList;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** 修正可否の選択肢 */
	public List<OptionItem> updateFlagList;

}
