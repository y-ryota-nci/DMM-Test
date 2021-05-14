package jp.co.nci.iwf.endpoint.mm.mm0013;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;

/**
 * ルックアップ登録リクエスト
 */
public class Mm0013InsertRequest extends BaseRequest {
	public MwmLookupEx lookup;
}
