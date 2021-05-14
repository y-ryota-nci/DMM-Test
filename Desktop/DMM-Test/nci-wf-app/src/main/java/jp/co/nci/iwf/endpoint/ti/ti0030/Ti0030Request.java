package jp.co.nci.iwf.endpoint.ti.ti0030;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 汎用テーブル設定一覧のリクエスト
 */
public class Ti0030Request extends BasePagingRequest {

	public String tableName;
	public String logicalTableName;
	public String entityType;
	public Long categoryId;
	public String displayName;
}
