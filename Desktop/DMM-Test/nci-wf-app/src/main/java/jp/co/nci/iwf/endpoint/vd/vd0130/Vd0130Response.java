package jp.co.nci.iwf.endpoint.vd.vd0130;

import java.util.List;
import java.util.Set;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * コンテナ設定のレスポンス
 */
public class Vd0130Response extends BaseResponse {
	/** フォントサイズの選択肢 */
	public List<OptionItem> fontSizes;
	/** DB予約語 */
	public Set<String> dbmsReservedColNames;
}
