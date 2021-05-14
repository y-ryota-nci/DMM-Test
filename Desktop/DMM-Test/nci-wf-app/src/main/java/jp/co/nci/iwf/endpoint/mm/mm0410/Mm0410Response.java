package jp.co.nci.iwf.endpoint.mm.mm0410;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 企業一覧のレスポンス
 */
public class Mm0410Response extends BasePagingResponse {

	public List<OptionItem> deleteFlags;
	public WfmCorporation corporation;
	public List<OptionItem> corporationGroups;
	public List<OptionItem> corporations;
}
