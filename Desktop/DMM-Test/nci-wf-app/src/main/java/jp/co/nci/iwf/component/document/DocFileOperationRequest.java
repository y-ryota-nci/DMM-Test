package jp.co.nci.iwf.component.document;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class DocFileOperationRequest extends BaseRequest {

	/** 文書ID */
	public Long docId;
	/** 会社コード */
	public String corporationCode;
	/** 文書ファイルID */
	public Long docFileId;
	/** バージョン */
	public Long version;
	/** 移動／コピー先の文書ID */
	public Long toDocId;
	/** バージョンのコピーをしないフラグ */
	public String notVersionCopy;

	/** 削除対象となる文書ファイル一覧 */
	public List<DocFileInfo> deleteDocFiles;
}
