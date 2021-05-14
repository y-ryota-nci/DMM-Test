package jp.co.nci.iwf.endpoint.mm.mm0050;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.mm.mm0051.Mm0051Entity;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 採番形式一覧のレスポンス
 */
public class Mm0050Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public Mm0051Entity numberingFormat;
}
