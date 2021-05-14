package jp.co.dmm.customize.endpoint.ri.ri0020;

import jp.co.dmm.customize.endpoint.ri.ri0010.Ri0010Entity;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 検収明細分割の検索リクエスト
 */
public class Ri0020Request extends BaseRequest {
	public Ri0010Entity source;
}
