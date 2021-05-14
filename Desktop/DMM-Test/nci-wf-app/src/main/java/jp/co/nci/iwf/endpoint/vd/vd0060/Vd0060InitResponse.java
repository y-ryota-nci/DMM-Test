package jp.co.nci.iwf.endpoint.vd.vd0060;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 選択肢一覧の初期化レスポンス
 */
public class Vd0060InitResponse extends BaseResponse {
	/** 企業の選択肢 */
	public List<OptionItem> corporations;

}
