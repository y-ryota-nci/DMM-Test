package jp.co.nci.iwf.endpoint.dc.dc0111;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;

/**
 * 画面文書定義設定の更新リクエスト
 */
public class Dc0111SaveRequest extends BaseRequest {
	/** */
	private static final long serialVersionUID = 1L;

	public MwmScreenDocDef entity;

}
