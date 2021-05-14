package jp.co.nci.iwf.endpoint.ml.ml0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メールテンプレート一覧の検索リクエスト
 */
public class Ml0010Request extends BasePagingRequest {

	public String corporationCode;
	public String mailTemplateFilename;
	public String remarks;
	public String sendFrom;
	public String mailSubject;
	public Long deleteMailTemplateFileId;

}
