package jp.co.nci.iwf.endpoint.dc.dc0906;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Dc0906Request extends BaseRequest {

	/** 検索文書フォルダ名 */
	public String folderName;
	/** 検索除外文書フォルダID */
	public Long exclusionDocFolderId;
}
