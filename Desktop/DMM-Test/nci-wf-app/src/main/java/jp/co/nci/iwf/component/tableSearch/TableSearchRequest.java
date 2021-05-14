package jp.co.nci.iwf.component.tableSearch;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 汎用テーブル検索画面の検索リクエスト
 */
public class TableSearchRequest extends BasePagingRequest {
	/** 汎用テーブル検索条件ID */
	public Long tableSearchId;
	/** テーブル名 */
	public String tableName;
	/** 検索条件カラム */
	public List<TableSearchConditionDef> conditionDefs;
	/** 検索結果カラム */
	public List<TableSearchResultDef> resultDefs;
	/** 検索条件 */
	public Map<String, String> conditions;
	/** デフォルトソートカラム(複数の場合はカンマ区切り) */
	public String defaultSortColumns;
	/** デフォルトソート方向(複数の場合はカンマ区切り) */
	public String defaultSortDirections;
}
