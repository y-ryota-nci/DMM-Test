package jp.co.nci.iwf.endpoint.cm.cm0170;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 要説明(掲示板)ブロック用、投稿内容入力画面のレスポンス
 */
public class Cm0170Response extends BaseResponse {
	/** プロセス掲示板メール区分の選択肢 */
	public List<OptionItem> processBbsMailTypes;
	/** 所属の選択肢 */
	public List<OptionItem> belongs;

}
