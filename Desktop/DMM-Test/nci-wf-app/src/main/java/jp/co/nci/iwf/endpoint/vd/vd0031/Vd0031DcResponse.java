package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面設定の表示条件レスポンス
 */
public class Vd0031DcResponse extends BaseResponse {
	/** 表示条件の選択肢 */
	public List<OptionItem> dcList;
}
