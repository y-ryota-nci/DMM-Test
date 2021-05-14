package jp.co.nci.iwf.endpoint.mm.mm0300;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 枝番更新用リクエスト
 */
public class Mm0300BranchRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String branchCorporationCode;
	public String branchProcessDefCode;
	public String branchProcessDefDetailCode;
	public Date updateValidStartDate;

}
