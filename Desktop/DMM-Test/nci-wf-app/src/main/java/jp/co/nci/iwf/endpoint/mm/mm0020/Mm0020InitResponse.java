package jp.co.nci.iwf.endpoint.mm.mm0020;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 表示条件設定の初期化レスポンス
 */
public class Mm0020InitResponse extends BaseResponse {
	/** コンテナ選択肢 */
	public List<OptionItem> containers;
	/** 表示条件区分の選択肢 */
	public List<OptionItem> dcTypes;
}
