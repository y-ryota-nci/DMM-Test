package jp.co.nci.iwf.endpoint.vd.vd0091;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessMenu;

/**
 * 新規申請メニュー割当設定の初期化レスポンス
 */
public class Vd0091InitResponse extends BaseResponse {

	public MwvScreenProcessMenu entity;
	public List<OptionItem> screenProcessDefs;

}
