package jp.co.dmm.customize.endpoint.mg.mg0300;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 住所マスタ情報アップロード画面の保存リクエスト
 */
public class Mg0300UploadSaveRequest extends BaseRequest {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** 未使用ならレコード削除するか */
	public boolean deleteIfNotUse;
	/** アップロードファイル名 */
	public String fileName;
}
