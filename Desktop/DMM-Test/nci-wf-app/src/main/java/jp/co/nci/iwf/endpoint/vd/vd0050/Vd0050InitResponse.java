package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 外部Javascript一覧の初期化レスポンス
 */
public class Vd0050InitResponse extends BaseResponse {
	/** 企業の選択肢 */
	public List<OptionItem> corporations;

}
