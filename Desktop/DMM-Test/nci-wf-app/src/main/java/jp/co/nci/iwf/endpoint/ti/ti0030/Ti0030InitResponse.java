package jp.co.nci.iwf.endpoint.ti.ti0030;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 汎用テーブル設定一覧の初期化レスポンス
 */
public class Ti0030InitResponse extends BaseResponse {

	public List<OptionItem> entityTypes;
	public List<OptionItem> categories;

}
