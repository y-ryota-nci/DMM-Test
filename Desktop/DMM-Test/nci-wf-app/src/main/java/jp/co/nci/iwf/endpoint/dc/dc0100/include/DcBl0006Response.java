package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class DcBl0006Response extends BaseResponse {

	/** 文書ファイル一覧 */
	public List<DocFileInfo> docFiles;
}
