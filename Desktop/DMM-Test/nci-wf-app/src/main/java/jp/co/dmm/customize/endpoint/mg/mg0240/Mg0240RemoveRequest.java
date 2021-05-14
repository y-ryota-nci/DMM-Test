package jp.co.dmm.customize.endpoint.mg.mg0240;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払サイトマスタ削除処理リクエスト
 *
 */
public class Mg0240RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
