package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class DcBl0005Response extends BaseResponse {

	/** 文書属性(拡張)項目一覧 */
	public List<DocAttributeExInfo> attributeExs;
}
