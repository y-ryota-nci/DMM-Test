package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツへのレスポンス
 */
public class Bl0002PartsResponse extends BaseResponse {
	/** パーツのHTML */
	public String html;

	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();
}
