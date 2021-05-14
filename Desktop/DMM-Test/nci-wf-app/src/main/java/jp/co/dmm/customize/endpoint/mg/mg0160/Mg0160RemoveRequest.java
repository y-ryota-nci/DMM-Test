package jp.co.dmm.customize.endpoint.mg.mg0160;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税マスタ削除処理リクエスト
 *
 */
public class Mg0160RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
