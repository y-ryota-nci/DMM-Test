package jp.co.nci.iwf.endpoint.mm.mm0012;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;

/**
 * ルックアップグループ設定レスポンス
 */
public class Mm0012Response extends BasePagingResponse {
	public MwmLookupGroup lookupGroup;

	public List<MwmLookupEx> lookupList;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

}
