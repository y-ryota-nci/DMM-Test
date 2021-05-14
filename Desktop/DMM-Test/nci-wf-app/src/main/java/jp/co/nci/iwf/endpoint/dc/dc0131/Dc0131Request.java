package jp.co.nci.iwf.endpoint.dc.dc0131;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleDoc;

/**
 * アクセス可能画面設定リクエスト
 */
public class Dc0131Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public String menuRoleCode;

	public List<MwmAccessibleDoc> accessibleDocs;

}
