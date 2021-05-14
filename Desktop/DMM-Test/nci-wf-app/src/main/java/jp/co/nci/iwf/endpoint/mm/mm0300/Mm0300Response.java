package jp.co.nci.iwf.endpoint.mm.mm0300;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ルート一覧レスポンス
 */
public class Mm0300Response extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 削除フラグ */
	public List<OptionItem> deleteFlags;

}
