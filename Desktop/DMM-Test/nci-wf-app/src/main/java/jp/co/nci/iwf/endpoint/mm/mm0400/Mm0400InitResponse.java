package jp.co.nci.iwf.endpoint.mm.mm0400;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 企業グループマスタ一覧の初期化レスポンス
 */
public class Mm0400InitResponse extends BaseResponse {
	public List<OptionItem> deleteFlags;
}
