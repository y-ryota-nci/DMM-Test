package jp.co.nci.iwf.endpoint.au.au0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ユーザパスワード覧のレスポンス
 */
public class Au0010Response extends BasePagingResponse {
	public List<OptionItem> corporations;
}
