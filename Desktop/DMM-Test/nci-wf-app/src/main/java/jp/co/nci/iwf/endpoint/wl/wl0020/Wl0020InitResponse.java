package jp.co.nci.iwf.endpoint.wl.wl0020;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 個人用トレイ選択画面の初期化レスポンス
 */
public class Wl0020InitResponse extends BaseResponse {

	public Map<TrayType, List<OptionItem>> trayConfigOptions;
	public List<Wl0020Entity> entities;

}
