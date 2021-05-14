package jp.co.nci.iwf.endpoint.up.up0040;

import java.util.List;

import jp.co.nci.iwf.endpoint.up.BaseUploadResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;

/**
 * トレイ表示設定アップロード画面のアップロード後レスポンス
 */
public class Up0040Response extends BaseUploadResponse {
	/** トレイ表示設定リスト */
	public List<MwmTrayConfig> configList;
	/** アップロードファイルID */
	public long uploadFileId;

}
