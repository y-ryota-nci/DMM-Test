package jp.co.nci.iwf.endpoint.dc.dc0110;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面文書定義一覧の初期化レスポンス
 */
public class Dc0110InitResponse extends BaseResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** 企業の選択肢 */
	public List<OptionItem> corporations;

}
