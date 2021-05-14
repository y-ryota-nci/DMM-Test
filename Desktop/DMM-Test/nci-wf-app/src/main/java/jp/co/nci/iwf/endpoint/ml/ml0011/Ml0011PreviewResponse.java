package jp.co.nci.iwf.endpoint.ml.ml0011;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メールテンプレート編集のプレビュー用レスポンス
 */
public class Ml0011PreviewResponse extends BaseResponse {

	public String mailBody;
	public String mailSubject;
}
