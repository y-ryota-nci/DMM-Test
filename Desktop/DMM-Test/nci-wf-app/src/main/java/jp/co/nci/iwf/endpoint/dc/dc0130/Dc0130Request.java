package jp.co.nci.iwf.endpoint.dc.dc0130;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 業務文書メニューロール一覧画面のリクエスト
 */
public class Dc0130Request extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String menuRoleCode;
	public String menuRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;

}
