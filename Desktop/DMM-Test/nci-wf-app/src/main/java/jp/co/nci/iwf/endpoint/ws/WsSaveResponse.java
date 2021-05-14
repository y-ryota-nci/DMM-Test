package jp.co.nci.iwf.endpoint.ws;

import java.util.List;

import jp.co.nci.integrated_workflow.model.view.WfvAuthTransfer;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 代理設定画面のレスポンス.
 */
public class WsSaveResponse extends BaseResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** 権限移譲マスタ */
	public WfvAuthTransfer target;
	/** 文書種別の選択肢 */
	public List<OptionItem> processDefs;

}