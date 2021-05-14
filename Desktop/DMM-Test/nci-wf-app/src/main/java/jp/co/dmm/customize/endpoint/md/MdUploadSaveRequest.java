package jp.co.dmm.customize.endpoint.md;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 取引先情報アップロード画面の保存リクエスト
 */
public class MdUploadSaveRequest extends BaseRequest {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** 未使用ならレコード削除するか */
	public boolean deleteIfNotUse;
	/** アップロードファイル名 */
	public String fileName;
}
