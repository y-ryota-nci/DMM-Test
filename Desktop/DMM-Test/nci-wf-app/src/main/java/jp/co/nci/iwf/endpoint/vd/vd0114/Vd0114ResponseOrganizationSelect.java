package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：組織選択パーツのレスポンス
 */
public class Vd0114ResponseOrganizationSelect extends BaseResponse {
	/** 幅の選択肢（レンダリング方法＝Bootstrapグリッド） */
	public List<OptionItem> columnSizes;
	/** デフォルト値の選択肢 */
	public List<OptionItem> defaultValues;
	/** ボタンサイズの選択肢 */
	public List<OptionItem> buttonSizes;
}
