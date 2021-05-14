package jp.co.dmm.customize.endpoint.mg.mg0320;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税関連マスタ削除処理リクエスト
 *
 */
public class Mg0320RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
