package jp.co.nci.iwf.endpoint.cm.cm0210;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書選択画面の初期化レスポンス
 */
public class Cm0210InitResponse extends BaseResponse {

	/** 文書種別(コンテンツ種別)の選択肢 */
	public List<OptionItem> contentsTypes;
	/** 公開区分の選択肢 */
	public List<OptionItem> publishFlags;
	/** 保存期間区分の選択肢 */
	public List<OptionItem> retentionTermTypes;

}
