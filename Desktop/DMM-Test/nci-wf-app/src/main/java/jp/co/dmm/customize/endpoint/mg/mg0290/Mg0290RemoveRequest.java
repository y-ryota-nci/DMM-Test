package jp.co.dmm.customize.endpoint.mg.mg0290;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 結合フロアマスタ削除処理リクエスト
 *
 */
public class Mg0290RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
