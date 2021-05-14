package jp.co.nci.iwf.endpoint.dc.dc0020;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Dc0020SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long docFolderId;
	public Long docId;
	public String keyword;
	public String searchType;
	public String includeDocFile;
}
