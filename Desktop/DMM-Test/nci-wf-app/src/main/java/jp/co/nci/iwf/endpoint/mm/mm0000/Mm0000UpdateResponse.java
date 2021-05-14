package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmCorporation;
import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.integrated_workflow.model.base.WfmUser;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル管理の更新結果レスポンス
 */
public class Mm0000UpdateResponse extends BaseResponse {
	public WfmUser newUser;
	public WfmOrganization newOrganization;
	public WfmCorporation newCorporation;
	public List<String> nodeIds;
}
