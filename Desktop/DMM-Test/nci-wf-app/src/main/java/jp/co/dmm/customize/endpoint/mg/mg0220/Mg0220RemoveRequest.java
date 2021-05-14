package jp.co.dmm.customize.endpoint.mg.mg0220;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払業務マスタ削除処理リクエスト
 *
 */
public class Mg0220RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
