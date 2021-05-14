package jp.co.nci.iwf.endpoint.dc.dc0050;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書フォルダ設定画面の編集リクエスト
 */
public class Dc0050EditRequest extends BaseRequest {

	/** 文書フォルダID */
	public Long docFolderId;
	/** 親文書フォルダID */
	public Long parentDocFolderId;
}
