package jp.co.nci.iwf.endpoint.mm.mm0411;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション設定レスポンス
 */
public class Mm0411Response extends BaseResponse {
	public WfmCorporation corporation;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;
}
