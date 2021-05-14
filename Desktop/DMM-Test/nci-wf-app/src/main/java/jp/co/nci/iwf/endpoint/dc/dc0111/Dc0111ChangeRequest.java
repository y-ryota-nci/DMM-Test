package jp.co.nci.iwf.endpoint.dc.dc0111;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面変更時のリクエスト.
 */
public class Dc0111ChangeRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public Long screenId;
}
