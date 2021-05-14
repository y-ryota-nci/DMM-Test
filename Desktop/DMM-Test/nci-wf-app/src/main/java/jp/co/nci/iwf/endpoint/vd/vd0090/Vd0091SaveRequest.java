package jp.co.nci.iwf.endpoint.vd.vd0090;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessMenu;

/**
 * 新規申請メニュー割当設定の保存リクエスト
 */
public class Vd0091SaveRequest extends BaseRequest {

	public MwvScreenProcessMenu entity;
}
