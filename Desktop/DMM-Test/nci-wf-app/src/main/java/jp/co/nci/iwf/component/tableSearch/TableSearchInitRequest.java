package jp.co.nci.iwf.component.tableSearch;

import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 汎用テーブル検索画面の初期化リクエスト
 */
public class TableSearchInitRequest extends BaseRequest {
	/** 汎用テーブル検索条件ID */
	public Long tableSearchId;
	/** 初期化パラメータMap */
	public Map<String, String> initConditions;
	/** 汎用テーブル名*/
	public String tableName;
	/** 企業コード */
	public String corporationCode;
	/** 汎用テーブル検索条件コード */
	public String tableSearchCode;
}
