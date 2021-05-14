package jp.co.nci.iwf.endpoint.mm.mm0001;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 企業編集画面の初期化レスポンス
 */
public class Mm0001InitResponse extends BaseResponse {
	public WfmCorporation corp;
	/** 企業グループの選択肢 */
	public List<OptionItem> corporationGroupCodes;
}
