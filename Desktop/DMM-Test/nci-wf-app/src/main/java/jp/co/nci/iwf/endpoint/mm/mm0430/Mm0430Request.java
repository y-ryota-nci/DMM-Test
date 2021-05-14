package jp.co.nci.iwf.endpoint.mm.mm0430;

import java.sql.Date;
import java.util.List;

import jp.co.nci.integrated_workflow.model.base.impl.WfmPostImpl;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 役職一覧のリクエスト
 */
public class Mm0430Request extends BasePagingRequest {

	public String corporationCode;
	public String postAddedInfo;
	public String postCode;
	public String postName;
	public String postNameAbbr;
	public String deleteFlag;
	public Date validStartDate;
	public Date validEndDate;
	public Long timestampUpdated;

	public List<WfmPostImpl> deletePosts;
}
