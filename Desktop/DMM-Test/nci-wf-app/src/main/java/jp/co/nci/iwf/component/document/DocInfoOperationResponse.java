package jp.co.nci.iwf.component.document;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書情報・文書ファイル情報の操作（ロック・ロック解除・削除・コピー・移動）を行う際の基底クラス.
 */
public class DocInfoOperationResponse extends BaseResponse {

	/** 文書情報 */
	public DocInfo docInfo;

}
