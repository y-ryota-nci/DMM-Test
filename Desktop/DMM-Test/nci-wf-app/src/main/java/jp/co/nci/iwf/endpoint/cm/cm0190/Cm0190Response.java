package jp.co.nci.iwf.endpoint.cm.cm0190;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfProcessableActivity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 遷移先アクティビティ選択画面のレスポンス
 */
public class Cm0190Response extends BaseResponse {

	public List<WfProcessableActivity> results;

}
