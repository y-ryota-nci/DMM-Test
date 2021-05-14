package jp.co.nci.iwf.endpoint.mm.mm0002;

import java.sql.Date;

import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 組織編集画面の更新リクエスト
 */
public class Mm0002UpdateRequest extends BaseRequest {
	public WfcOrganization org;
	public Date baseDate;
}
