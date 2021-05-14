package jp.co.nci.iwf.endpoint.vd.vd0033;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面Javascript設定画面の初期化リクエスト
 */
public class Vd0033InitRequest extends BaseRequest {

	public List<PartsJavascript> scripts;
}
