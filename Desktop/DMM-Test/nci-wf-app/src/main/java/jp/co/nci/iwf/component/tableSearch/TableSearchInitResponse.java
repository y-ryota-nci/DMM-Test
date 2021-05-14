package jp.co.nci.iwf.component.tableSearch;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 汎用テーブル検索画面の初期化リクエスト
 */
public class TableSearchInitResponse extends BaseResponse {
	/** 汎用テーブル検索条件ID */
	public Long tableSearchId;
	/** テーブル名 */
	public String tableName;
	/** 初期検索の有無 */
	public boolean initSearch;
	/** 検索条件カラム */
	public List<TableSearchConditionDef> conditionDefs;
	/** 検索結果カラム */
	public List<TableSearchResultDef> resultDefs;
	/** デフォルトソートカラム(複数の場合はカンマ区切り) */
	public String defaultSortColumns;
	/** デフォルトソート方向(複数の場合はカンマ区切り) */
	public String defaultSortDirections;
}
