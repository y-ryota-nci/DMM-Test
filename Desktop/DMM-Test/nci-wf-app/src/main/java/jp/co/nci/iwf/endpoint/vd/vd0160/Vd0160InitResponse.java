package jp.co.nci.iwf.endpoint.vd.vd0160;

import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツツリー画面の初期化レスポンス
 */
public class Vd0160InitResponse extends BaseResponse {
	/** 業務管理項目の選択肢 */
	public Map<String, String> businessInfoCodes;

}
