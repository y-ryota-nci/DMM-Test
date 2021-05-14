package jp.co.nci.iwf.endpoint.dc.dc0902;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書ファイルの更新画面用の初期化レスポンス
 */
public class Dc0902Response extends BaseResponse {

	/** 文書フォルダの選択肢 */
	public List<OptionItem> folders;
}
