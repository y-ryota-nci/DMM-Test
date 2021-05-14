package jp.co.nci.iwf.endpoint.mm.mm0401;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 企業グループマスタ設定の初期化リクエスト
 */
public class Mm0401InitRequest extends BaseRequest {
	public String corporationGroupCode;
	public Long timestampUpdated;
}
