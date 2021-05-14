package jp.co.dmm.customize.endpoint.mg.mg0170;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 通貨マスタ削除処理リクエスト
 *
 */
public class Mg0170RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
