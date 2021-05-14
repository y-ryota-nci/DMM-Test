package jp.co.nci.iwf.endpoint.ml.ml0011;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メールテンプレート編集の保存リクエスト
 */
public class Ml0011SaveRequest extends BaseRequest {

	/** メールテンプレートのヘッダ情報 */
	public Ml0011EntityHeader header;
	/** メールテンプレートの本文情報 */
	public List<Ml0011EntityBody> bodies;
}
