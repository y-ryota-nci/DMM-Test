package jp.co.dmm.customize.endpoint.md;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 取引先情報アップロード画面のレスポンス
 */
public class MdUploadResponse extends BaseResponse {

	/** エンコード */
	public String encoded;
	/** アップロードファイルのファイル名 */
	public String fileName;

	/** 取引先マスタ総件数 */
	public long splrCount;
	/** 振込先マスタ総件数 */
	public long payeeBnkaccCount;
	/** 関係先マスタ総件数 */
	public long rltPrtCount;
	/** 反社情報総件数 */
	public long orgCrmCount;
}
