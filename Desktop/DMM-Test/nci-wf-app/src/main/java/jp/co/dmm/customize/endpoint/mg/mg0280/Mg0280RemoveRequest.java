package jp.co.dmm.customize.endpoint.mg.mg0280;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メディアマスタ削除処理リクエスト
 *
 */
public class Mg0280RemoveRequest extends BasePagingRequest {

	/** 削除対象 */
	public String deleteTarget;

}
