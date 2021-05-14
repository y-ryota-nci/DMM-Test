package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：ドロップダウンリストパーツのレスポンス
 */
public class Vd0114ResponseDropdown extends BaseResponse {

	/** パーツ選択肢リスト */
	public List<OptionItem> options;
	/** デフォルト値の選択肢 */
	public List<PartsOptionItem> defaultValueList;
	/** 未選択行の表示の選択肢 */
	public List<OptionItem> emptyLineTypes;

}
