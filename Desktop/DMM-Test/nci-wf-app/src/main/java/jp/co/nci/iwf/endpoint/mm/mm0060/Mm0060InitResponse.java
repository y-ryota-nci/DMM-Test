package jp.co.nci.iwf.endpoint.mm.mm0060;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ブロック表示順設定の初期化レスポンス
 */
public class Mm0060InitResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 画面プロセス定義選択肢 */
	public List<OptionItem> screenProcesses;

}
