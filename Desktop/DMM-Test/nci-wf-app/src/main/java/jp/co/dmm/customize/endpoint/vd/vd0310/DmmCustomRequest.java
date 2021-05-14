package jp.co.dmm.customize.endpoint.vd.vd0310;

import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * DMMカスタムリクエスト（VD0310用）
 */
public class DmmCustomRequest extends BaseRequest {
	/** CDIメソッド名 */
	public String methodName;
	/** CDIメソッドに渡す引数 */
	public Map<String, Object> parameters;
}
