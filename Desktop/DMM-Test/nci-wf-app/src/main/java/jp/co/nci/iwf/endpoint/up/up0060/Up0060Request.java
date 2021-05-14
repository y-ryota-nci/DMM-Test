package jp.co.nci.iwf.endpoint.up.up0060;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * システム環境設定アップロード画面の保存リクエスト
 */
public class Up0060Request extends BaseRequest {
	/** 企業プロパティマスタを取り込むか */
	public boolean enableCorporationProperty;
	/** プロパティマスタを取り込むか */
	public boolean enableCorpPropMaster;
	/** メール環境設定マスタを取り込むか */
	public boolean enableMailConfig;
	/** アップロードファイルID */
	public Long uploadFileId;
}
