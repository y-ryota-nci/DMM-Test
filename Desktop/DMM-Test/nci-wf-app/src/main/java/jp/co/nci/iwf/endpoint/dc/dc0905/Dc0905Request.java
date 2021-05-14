package jp.co.nci.iwf.endpoint.dc.dc0905;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Dc0905Request extends BaseRequest {

	/** 検索文書フォルダ名 */
	public String folderName;
	/** 検索除外文書フォルダID */
	public Long exclusionDocFolderId;
}
