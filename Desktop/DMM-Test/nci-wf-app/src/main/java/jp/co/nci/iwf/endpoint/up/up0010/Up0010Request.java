package jp.co.nci.iwf.endpoint.up.up0010;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0030.Up0010SaveConfig;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面定義アップロード画面の保存リクエスト
 */
public class Up0010Request extends BaseRequest {

	/** 取込設定リスト */
	public List<Up0010SaveConfig> configs;
	/** アップロードファイルID */
	public Long uploadFileId;
}
