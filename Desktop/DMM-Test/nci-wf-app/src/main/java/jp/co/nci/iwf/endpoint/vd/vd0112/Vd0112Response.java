package jp.co.nci.iwf.endpoint.vd.vd0112;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Vd0112Response extends BaseResponse {

	/** 論理演算子一覧 */
	public Map<String, String> logiclOperators;
	/** 括弧一覧 */
	public Map<String, String> parentheses;
	/** 比較演算子一覧 */
	public List<OptionItem> comparisonOperators;
}
