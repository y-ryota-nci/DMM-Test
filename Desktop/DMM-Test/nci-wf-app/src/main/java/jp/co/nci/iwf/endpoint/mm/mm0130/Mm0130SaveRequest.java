package jp.co.nci.iwf.endpoint.mm.mm0130;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メニュー編集の更新リクエスト
 */
public class Mm0130SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	//public List<MwmMenu> inputs;
	public String menuId;
	public String menuName;
}
