package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.List;

import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;

/**
 * 文書属性ブロック：レスポンス
 */
public class DcBl0004Response extends BaseResponse {

	/** 文書フォルダ情報 */
	public MwvDocFolder folder;
	/** 文書権限情報一覧 */
	public List<DocAccessibleInfo> accessibles;
	/** 文書属性(拡張)メタ項目一覧 */
	public List<DocAttributeExInfo> attributeExs;

}
