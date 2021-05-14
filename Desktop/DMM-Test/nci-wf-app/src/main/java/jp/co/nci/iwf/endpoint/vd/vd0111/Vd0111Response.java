package jp.co.nci.iwf.endpoint.vd.vd0111;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Vd0111Response extends BaseResponse {

	/** 設定可能なパーツ条件定義一覧. */
	public List<PartsCond> templates;
	/** 論理演算子一覧 */
	public Map<String, String> logicalOperators;
	/** 括弧一覧 */
	public Map<String, String> parentheses;
	/** 比較演算子一覧 */
	public Map<String, String> comparisonOperators;
}
