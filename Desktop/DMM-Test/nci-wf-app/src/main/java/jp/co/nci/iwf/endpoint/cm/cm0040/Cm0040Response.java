package jp.co.nci.iwf.endpoint.cm.cm0040;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ユーザ選択のレスポンス
 */
public class Cm0040Response extends BasePagingResponse {

	public List<OptionItem> corporations;
}
