package jp.co.nci.iwf.endpoint.vd.vd0041;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面プロセス定義設定のプロセス定義明細コード選択肢取得レスポンス
 */
public class Vd0041DetailResponse extends BaseResponse {

	public List<OptionItem> processDefDetailCodes;
}
