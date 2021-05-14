package jp.co.nci.iwf.endpoint.mm.mm0040;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 業務管理項目名称設定の更新リクエスト
 */
public class Mm0040SaveRequest extends BaseRequest {
	/** 業務管理項目名称設定リスト */
	public List<MwmDocBusinessInfoName> businessInfoNameList;
}
