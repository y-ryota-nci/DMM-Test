package jp.co.nci.iwf.component.document;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class DocFolderSearchRequest extends BasePagingRequest {

	/** 企業コード */
	public String corporationCode;
	/** 親文書フォルダID */
	public Long parentDocFolderId;
	/** 文書フォルダID */
	public Long docFolderId;
	/** フォルダコード */
	public String folderCode;
	/** フォルダ名 */
	public String folderName;
	/** 除外する文書フォルダID */
	public Long excludeDocFolderId;
}
