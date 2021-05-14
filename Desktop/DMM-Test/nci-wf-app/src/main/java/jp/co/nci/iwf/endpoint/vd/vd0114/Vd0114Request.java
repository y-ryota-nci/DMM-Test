package jp.co.nci.iwf.endpoint.vd.vd0114;

import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パーツプロパティ設定画面リクエスト
 */
public class Vd0114Request extends BaseRequest {
	/** パーツ定義 */
	public PartsDesign design;
	/** テーブル名 */
	public String tableName;
}
