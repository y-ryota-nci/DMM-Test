package jp.co.nci.iwf.endpoint.mm.mm0101;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 情報共有者定義作成の検索リクエスト
 */
public class Mm0101SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String assignRoleCode;
	public String assignRoleName;
	public Date validStartDate;
	public Date validEndDate;

}