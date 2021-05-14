package jp.co.nci.iwf.endpoint.mm.mm0013;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupEx;

/**
 * ルックアップ設定レスポンス
 */
public class Mm0013Response extends BaseResponse {
	public MwmLookupEx lookup;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

}
