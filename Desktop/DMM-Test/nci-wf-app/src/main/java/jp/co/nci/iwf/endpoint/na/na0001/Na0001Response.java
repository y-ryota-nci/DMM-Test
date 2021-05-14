package jp.co.nci.iwf.endpoint.na.na0001;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * メニューロール一覧のレスポンス
 */
public class Na0001Response extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 4737561413249088894L;

	public List<OptionItem> deleteFlags;

}