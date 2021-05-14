package jp.co.nci.iwf.endpoint.mm.mm0440;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ユーザ一覧画面の初期化レスポンス
 */
public class Mm0440InitResponse extends BaseResponse {
	/** 企業の選択肢 */
	public List<OptionItem> corporations;
	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlags;
	/** 言語の選択肢 */
	public List<OptionItem> localeCodes;

}
