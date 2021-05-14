package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面レスポンス
 */
public class Vd0114Response extends BaseResponse {
	/** フォントサイズの選択肢 */
	public List<OptionItem> fontSizes;
	/** 幅の選択肢（レンダリング方法＝Bootstrapグリッド） */
	public List<OptionItem> columnSizes;
	/** 幅の選択肢（レンダリング方法＝インライン） */
	public List<OptionItem> columnSizesInline;
	/** レンダリング方法の選択肢 */
	public List<OptionItem> renderingMethods;
	/** 業務管理項目の選択肢 */
	public List<OptionItem> businessInfoCodes;
	/** 文書管理項目の選択肢 */
	public List<OptionItem> docBusinessInfoCodes;
}
