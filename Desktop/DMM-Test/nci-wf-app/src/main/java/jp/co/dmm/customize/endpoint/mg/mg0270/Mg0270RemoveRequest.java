package jp.co.dmm.customize.endpoint.mg.mg0270;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 予算科目マスタ削除処理リクエスト
 *
 */
public class Mg0270RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
