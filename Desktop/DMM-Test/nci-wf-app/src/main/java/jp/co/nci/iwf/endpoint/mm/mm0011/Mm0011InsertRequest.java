package jp.co.nci.iwf.endpoint.mm.mm0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupGroupEx;

/**
 * ルックアップグループの登録リクエスト
 */
public class Mm0011InsertRequest extends BaseRequest {
	public MwmLookupGroupEx lookupGroup;
}
