package jp.co.nci.iwf.endpoint.ml.ml0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メールテンプレート編集のプレビュー用リクエスト
 */
public class Ml0011PreviewRequest extends BaseRequest {

	public String mailBody;
	public String mailSubject;
	public String localeCode;
}
