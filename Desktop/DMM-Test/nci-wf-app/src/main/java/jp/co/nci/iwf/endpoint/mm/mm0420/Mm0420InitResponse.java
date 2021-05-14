package jp.co.nci.iwf.endpoint.mm.mm0420;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 組織一覧画面の初期化レスポンス
 */
public class Mm0420InitResponse extends BaseResponse {

	/** 企業の選択肢 */
	public List<OptionItem> corporations;
	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlags;
	/** 言語の選択肢 */
	public List<OptionItem> localeCodes;
}
