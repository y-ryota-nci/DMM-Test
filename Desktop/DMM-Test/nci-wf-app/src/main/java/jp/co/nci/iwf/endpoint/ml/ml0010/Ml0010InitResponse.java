package jp.co.nci.iwf.endpoint.ml.ml0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メールテンプレート一覧の初期化レスポンス
 */
public class Ml0010InitResponse extends BaseResponse {
	/** 企業の選択肢 */
	public List<OptionItem> corporations;

}
