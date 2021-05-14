package jp.co.nci.iwf.endpoint.rm.rm0111;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;

/**
 * 業務管理項目名称設定の更新リクエスト
 */
public class Rm0111SaveRequest extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
	public List<MwmMenuEx> accessibleMenuList;

}
