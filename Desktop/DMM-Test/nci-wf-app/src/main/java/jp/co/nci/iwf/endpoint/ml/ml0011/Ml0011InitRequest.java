package jp.co.nci.iwf.endpoint.ml.ml0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メールテンプレート編集の初期化リクエスト
 */
public class Ml0011InitRequest extends BaseRequest {

	public Long fileId;
	public Long headerId;
	public Long version;
	public boolean backToStandard;
}
