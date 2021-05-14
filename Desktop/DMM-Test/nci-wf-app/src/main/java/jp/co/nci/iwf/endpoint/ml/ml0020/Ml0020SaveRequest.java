package jp.co.nci.iwf.endpoint.ml.ml0020;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メール環境設定の保存リクエスト
 */
public class Ml0020SaveRequest extends BaseRequest {
	public List<Ml0020Entity> inputs;
}
