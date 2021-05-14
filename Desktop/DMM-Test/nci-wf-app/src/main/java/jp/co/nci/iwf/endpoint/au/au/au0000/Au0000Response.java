package jp.co.nci.iwf.endpoint.au.au.au0000;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * なりすまし先選択画面レスポンス
 */
public class Au0000Response extends BaseResponse {

	public String corporationCode;
	public String userCode;
	public String userName;
	public String userAddedInfo;
	public List<OptionItem> corporations;
	public String organizationCode;
	public String organizationAddedInfo;
	public String organizationTreeName;

}
