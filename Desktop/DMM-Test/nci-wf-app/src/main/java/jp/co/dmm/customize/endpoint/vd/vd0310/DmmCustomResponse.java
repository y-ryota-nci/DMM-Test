package jp.co.dmm.customize.endpoint.vd.vd0310;

import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * DMMカスタムリクエスト（VD0310用）
 */
public class DmmCustomResponse extends BaseResponse {
	/** 処理結果Map */
	public Map<String, Object> results = new HashMap<>();
}
