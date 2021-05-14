package jp.co.nci.iwf.endpoint.vd.vd0180;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パーツコード変更画面リクエスト
 */
public class Vd0180Request extends BaseRequest {
	public String newPartsCode;
	public String oldPartsCode;
	public DesignerContext ctx;
}
