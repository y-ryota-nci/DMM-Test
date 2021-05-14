package jp.co.nci.iwf.component.document;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class AttachFileDocRequest extends BaseRequest {

	/** 文書ID */
	public Long docId;
	/** 文書添付ファイルID */
	public Long attachFiledocId;
	/** バージョン */
	public Long version;

	/** 削除対象となる文書ファイル一覧 */
	public List<AttachFileDocInfo> deleteAttachFiles;

//	/** 削除対象となる文書ファイルID一覧 */
//	public List<Long> deleteDocFileIds;
//	/** 削除対象となる文書ファイルデータID一覧 */
//	public List<Long> deleteDocFileDataIds;
}
