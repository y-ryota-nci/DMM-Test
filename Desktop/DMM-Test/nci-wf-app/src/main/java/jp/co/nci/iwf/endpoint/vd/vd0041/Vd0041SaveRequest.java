package jp.co.nci.iwf.endpoint.vd.vd0041;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * 画面プロセス定義設定の更新リクエスト
 */
public class Vd0041SaveRequest extends BaseRequest {
	public MwmScreenProcessDef entity;
}
