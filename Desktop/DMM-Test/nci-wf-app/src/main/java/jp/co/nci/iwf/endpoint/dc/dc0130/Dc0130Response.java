package jp.co.nci.iwf.endpoint.dc.dc0130;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 業務文書メニューロール一覧画面のレスポンス
 */
public class Dc0130Response extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 4737561413249088894L;

	public List<OptionItem> deleteFlags;

}