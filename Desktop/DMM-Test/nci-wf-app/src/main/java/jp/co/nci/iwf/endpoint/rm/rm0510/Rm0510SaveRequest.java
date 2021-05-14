package jp.co.nci.iwf.endpoint.rm.rm0510;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;

/**
 * 業務管理項目名称設定の更新リクエスト
 */
public class Rm0510SaveRequest extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
	public List<MwmMenuEx> accessibleMenuList;

}
