package jp.co.nci.iwf.endpoint.ti.ti0020;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メニューロール一覧（マスタ権限設定）リクエスト
 */
public class Ti0020Request extends BasePagingRequest {

	public String menuRoleCode;
	public String menuRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;
}
