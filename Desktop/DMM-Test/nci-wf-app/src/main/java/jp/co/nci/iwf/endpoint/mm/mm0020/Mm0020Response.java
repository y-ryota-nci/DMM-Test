package jp.co.nci.iwf.endpoint.mm.mm0020;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 表示条件設定のレスポンス
 */
public class Mm0020Response extends BaseResponse {

	public List<Mm0020Dc> cols;
	public List<Map<String, Object>> rows;
}
