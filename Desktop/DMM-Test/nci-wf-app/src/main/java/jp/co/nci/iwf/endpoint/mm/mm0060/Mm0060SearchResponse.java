package jp.co.nci.iwf.endpoint.mm.mm0060;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ブロック表示順設定の検索レスポンス
 */
public class Mm0060SearchResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<Mm0060Dc> cols;
	public List<Map<String, Object>> rows;

}
