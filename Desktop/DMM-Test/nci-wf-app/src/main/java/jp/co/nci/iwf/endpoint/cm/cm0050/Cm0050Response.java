package jp.co.nci.iwf.endpoint.cm.cm0050;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ユーザ所属選択のレスポンス
 */
public class Cm0050Response extends BasePagingResponse {

	public List<OptionItem> corporations;

}
