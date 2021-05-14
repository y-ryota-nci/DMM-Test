package jp.co.nci.iwf.endpoint.dc.dc0101;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書ファイルの更新画面用リクエスト
 */
public class Dc0101ExecuteRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書ファイル情報 */
	public DocFileInfo docFileInfo;
	/** バージョン更新区分 */
	public String updateVersionType;
}
