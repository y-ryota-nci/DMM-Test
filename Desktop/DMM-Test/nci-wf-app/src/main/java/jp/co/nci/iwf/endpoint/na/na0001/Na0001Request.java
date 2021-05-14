package jp.co.nci.iwf.endpoint.na.na0001;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メニューロール一覧画面のリクエスト
 */
public class Na0001Request extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String menuRoleCode;
	public String menuRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;

}
