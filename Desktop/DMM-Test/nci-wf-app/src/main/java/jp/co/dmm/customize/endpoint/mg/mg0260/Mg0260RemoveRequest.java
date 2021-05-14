package jp.co.dmm.customize.endpoint.mg.mg0260;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ｸﾚｶ口座マスタ削除処理リクエスト
 *
 */
public class Mg0260RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
