package jp.co.nci.iwf.endpoint.mm.mm0012;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupGroupEx;

/**
 * ルックアップグループ設定の更新リクエスト
 */
public class Mm0012UpdateRequest extends BaseRequest {
	public MwmLookupGroupEx lookupGroup;
}
