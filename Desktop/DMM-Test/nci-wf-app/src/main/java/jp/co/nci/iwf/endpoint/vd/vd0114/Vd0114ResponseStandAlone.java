package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 独立画面パーツの初期化レスポンス
 */
public class Vd0114ResponseStandAlone extends BaseResponse {

	/** ボタンサイズの選択肢 */
	public List<OptionItem> buttonSizes;
	/** 子コンテナの選択肢 */
	public List<OptionItem> childContainers;
}
