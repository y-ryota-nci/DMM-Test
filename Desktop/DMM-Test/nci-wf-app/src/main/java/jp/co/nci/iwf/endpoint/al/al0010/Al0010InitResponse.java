package jp.co.nci.iwf.endpoint.al.al0010;

import java.sql.Date;
import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクセスログ検索の初期化レスポンス
 */
public class Al0010InitResponse extends BaseResponse {
	/** 結果種別の選択肢 */
	public List<OptionItem> accessLogResultTypes;
	/** システム日付 */
	public Date today;
	/** システム時刻 */
	public String now;
	/** 企業の選択肢 */
	public List<OptionItem> corporations;
}
