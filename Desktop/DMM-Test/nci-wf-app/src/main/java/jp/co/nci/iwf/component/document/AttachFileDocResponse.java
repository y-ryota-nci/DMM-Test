package jp.co.nci.iwf.component.document;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class AttachFileDocResponse extends BaseResponse {

	/** 文書添付ファイル一覧 */
	public List<AttachFileDocInfo> attachFiles;

//	/** 削除された文書ファイルID一覧 */
//	public List<Long> deleteDocFileIds;
//	/** 削除された文書ファイルデータID一覧 */
//	public List<Long> deleteDocFileDataIds;
}
