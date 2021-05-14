package jp.co.nci.iwf.component.document;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書情報の操作（ロック・ロック解除・削除・コピー・移動）を行う際の基底クラス.
 */
public class DocInfoOperationRequest extends BaseRequest {

	/** 文書ID */
	public Long docId;
	/** バージョン */
	public Long version;
	/** 会社コード */
	public String corporationCode;
	/** 文書情報の移動先またはコピー先の文書フォルダID */
	public Long docFolderIdTo;
	/** コピー時の文書情報の件名 */
	public String newTitle;
}
