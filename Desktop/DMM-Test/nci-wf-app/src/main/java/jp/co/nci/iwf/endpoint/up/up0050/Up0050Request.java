package jp.co.nci.iwf.endpoint.up.up0050;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メールテンプレート定義アップロード画面の更新リクエスト
 */
public class Up0050Request extends BaseRequest {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** 取込対象とするメールテンプレートファイル名 */
	public Set<String> filenames;
	/** アップロードファイルID */
	public Long uploadFileId;

}
