package jp.co.nci.iwf.endpoint.mm.mm0401;

import jp.co.nci.integrated_workflow.model.custom.WfcCorporationGroup;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 企業グループマスタ設定の保存リクエスト
 */
public class Mm0401Request extends BaseRequest {
	public WfcCorporationGroup corporationGroup;
}
