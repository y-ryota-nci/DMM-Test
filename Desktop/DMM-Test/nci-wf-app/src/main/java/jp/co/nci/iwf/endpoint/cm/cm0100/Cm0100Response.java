package jp.co.nci.iwf.endpoint.cm.cm0100;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 参加者変更ユーザ検索のレスポンス
 */
public class Cm0100Response extends BasePagingResponse {

	public List<OptionItem> changeDefList;

}
