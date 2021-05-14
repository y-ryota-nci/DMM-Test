package jp.co.nci.iwf.component.document;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class DocFileOperationResponse extends BaseResponse {

	/** 文書ファイル一覧 */
	public List<DocFileInfo> docFiles;

//	/** 削除された文書ファイルID一覧 */
//	public List<Long> deleteDocFileIds;
//	/** 削除された文書ファイルデータID一覧 */
//	public List<Long> deleteDocFileDataIds;
}
