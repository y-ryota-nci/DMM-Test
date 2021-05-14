package jp.co.nci.iwf.endpoint.dc.dc0022;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 書類一覧画面の初期化レスポンス
 */
public class Dc0022InitResponse extends BaseResponse {

	/** 文書種別(コンテンツ種別)の選択肢 */
	public List<OptionItem> contentsTypes;
	/** 公開区分の選択肢 */
	public List<OptionItem> publishFlags;
	/** 保存期間区分の選択肢 */
	public List<OptionItem> retentionTermTypes;

}
