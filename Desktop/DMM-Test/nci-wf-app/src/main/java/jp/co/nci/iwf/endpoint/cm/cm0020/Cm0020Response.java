package jp.co.nci.iwf.endpoint.cm.cm0020;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 組織選択のレスポンス
 */
public class Cm0020Response extends BasePagingResponse {

	public List<OptionItem> deleteFlags;
	public List<OptionItem> corporations;
}
