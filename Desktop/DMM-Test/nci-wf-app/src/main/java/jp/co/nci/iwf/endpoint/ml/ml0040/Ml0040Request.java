package jp.co.nci.iwf.endpoint.ml.ml0040;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;

/**
 * メール変数設定リクエスト
 */
public class Ml0040Request extends BaseRequest {

	public List<MwmMailVariable> inputs;
}
