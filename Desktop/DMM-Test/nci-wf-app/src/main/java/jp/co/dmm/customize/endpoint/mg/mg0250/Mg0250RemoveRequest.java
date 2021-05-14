package jp.co.dmm.customize.endpoint.mg.mg0250;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 部門関連マスタ削除処理リクエスト
 *
 */
public class Mg0250RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
