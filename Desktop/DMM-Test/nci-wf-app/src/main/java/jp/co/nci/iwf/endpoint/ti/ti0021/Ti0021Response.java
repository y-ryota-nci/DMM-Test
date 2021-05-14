package jp.co.nci.iwf.endpoint.ti.ti0021;

import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * マスタ権限設定のレスポンス
 */
public class Ti0021Response extends BaseResponse {

	public WfmMenuRole menuRole;
	public List<Ti0021Category> categories;
	public Map<Long, List<Ti0021Table>> tables;

}
