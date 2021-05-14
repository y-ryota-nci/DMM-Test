package jp.co.nci.iwf.endpoint.mm.mm0091;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ルックアップグループ登録レスポンス
 */
public class Mm0091Response extends BaseResponse {
	public WfmLookupType lookupType;
	public List<OptionItem> updateFlags;
}
