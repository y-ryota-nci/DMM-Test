package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：選択肢変更時のレスポンス
 */
public class Vd0114ResponseOptionChange extends BaseResponse {

	/** デフォルト値の選択肢 */
	public List<PartsOptionItem> defaultValueList;

}
