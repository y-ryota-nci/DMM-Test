package jp.co.nci.iwf.endpoint.vd.vd0030;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面一覧の初期化レスポンス
 */
public class Vd0030InitResponse extends BaseResponse {
	/** スクラッチ区分の選択肢 */
	public List<OptionItem> scratchFlags;
	/** 企業コードの選択肢 */
	public List<OptionItem> corporations;
}
