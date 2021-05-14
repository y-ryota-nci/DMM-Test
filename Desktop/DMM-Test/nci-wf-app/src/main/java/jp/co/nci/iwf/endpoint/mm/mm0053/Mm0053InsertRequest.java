package jp.co.nci.iwf.endpoint.mm.mm0053;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;

/**
 * 通し番号登録リクエスト
 */
public class Mm0053InsertRequest extends BaseRequest {
	public MwmPartsSequenceSpec sequence;
}
