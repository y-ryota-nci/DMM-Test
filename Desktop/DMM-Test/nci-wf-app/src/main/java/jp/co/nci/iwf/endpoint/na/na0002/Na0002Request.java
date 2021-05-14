package jp.co.nci.iwf.endpoint.na.na0002;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleScreen;

/**
 * アクセス可能画面設定リクエスト
 */
public class Na0002Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public String menuRoleCode;

	public List<MwmAccessibleScreen> accessibleScreens;

}
