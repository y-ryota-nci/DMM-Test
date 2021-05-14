package jp.co.nci.iwf.endpoint.vd.vd0040;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面プロセス定義一覧の初期化レスポンス
 */
public class Vd0040InitResponse extends BaseResponse {
	/** 企業の選択肢 */
	public List<OptionItem> corporations;
	/** プロセス定義の選択肢 */
	public List<OptionItem> processDefs;

}
