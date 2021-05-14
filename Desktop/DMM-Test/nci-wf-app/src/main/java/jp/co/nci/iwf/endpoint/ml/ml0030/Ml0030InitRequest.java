package jp.co.nci.iwf.endpoint.ml.ml0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メールテンプレートファイル設定の初期化リクエスト
 */
public class Ml0030InitRequest extends BaseRequest {

	public Long mailTemplateFileId;
	public Long version;
}
