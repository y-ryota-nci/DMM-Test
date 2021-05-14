package jp.co.nci.iwf.endpoint.vd.vd0144;

import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Vd0144Response extends BaseResponse {

	/** 算術演算子一覧 */
	public Map<String, String> arithmeticOperators;
	/** 括弧一覧 */
	public Map<String, String> parentheses;
}
