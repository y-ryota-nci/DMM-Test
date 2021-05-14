package jp.co.nci.iwf.component.tableSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;

/**
 * 汎用テーブルのサービス
 */
@BizLogic
public class TableSearchService extends BasePagingService {
	@Inject private TableSearchRepository repository;
	@Inject private CacheManager cm;
	@Inject private TableMetaDataService meta;

	/** キャッシュ：汎用テーブル検索条件 */
	private CacheHolder<Long, MwmTableSearchEx> cacheTableSearch;
	/** キャッシュ：汎用テーブル検索条件カラム */
	private CacheHolder<Long, List<MwmTableSearchColumnEx>> cacheCols;
	/** キャッシュ：選択肢マスタ */
	private CacheHolder<Long, List<OptionItem>> cacheOptions;
	/** キャッシュ：汎用テーブルの検索結果 */
	private CacheHolder<String, List<TableSearchEntity>> cacheSearchResults;

	/** 初期化 */
	@PostConstruct
	public void init() {
		// 汎用テーブル検索条件
		cacheTableSearch = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		// 汎用テーブル検索条件カラム
		cacheCols = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		// 選択肢マスタ
		cacheOptions = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		// 汎用テーブルの検索結果
		cacheSearchResults = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/** リソース解放 */
	@PreDestroy
	public void dispose() {
		// 汎用テーブル検索条件
		cm.remove(cacheTableSearch);
		cacheTableSearch.dispose();
		cacheTableSearch = null;

		// 汎用テーブル検索条件カラム
		cm.remove(cacheCols);
		cacheCols.dispose();
		cacheCols = null;

		// 選択肢マスタ
		cm.remove(cacheOptions);
		cacheOptions.dispose();
		cacheOptions = null;
	}

	/**
	 * キャッシュから汎用テーブル検索条件を抽出
	 * @param tableSearchId 汎用テーブル検索条件ID
	 * @param localeCode 言語コード
	 * @return
	 */
	private MwmTableSearchEx getMwmTableSearch(long tableSearchId, String localeCode) {
		MwmTableSearchEx ts = cacheTableSearch.get(tableSearchId);
		if (ts == null) {
			synchronized (cacheTableSearch) {
				ts = cacheTableSearch.get(tableSearchId);
				if (ts == null) {
					ts = repository.getMwmTableSearch(tableSearchId, localeCode);
					if (ts != null) {
						cacheTableSearch.put(tableSearchId, ts);
					}
				}
			}
		}
		return ts;
	}

	/** キャッシュから汎用テーブル検索条件カラムを抽出 */
	private List<MwmTableSearchColumnEx> getColumns(MwmTableSearchEx ts, String localeCode) {
		List<MwmTableSearchColumnEx> cols = cacheCols.get(ts.tableSearchId);
		if (cols == null) {
			synchronized (cacheCols) {
				cols = cacheCols.get(ts.tableSearchId);
				if (cols == null) {
					cols = repository.getColumns(ts.tableSearchId, ts.corporationCode, localeCode);
					cacheCols.put(ts.tableSearchId, cols);
				}
			}
		}
		return cols;
	}

	/** キャッシュから選択肢マスタを抽出  */
	private List<OptionItem> getMwmOptionItems(long optionId) {
		List<OptionItem> options = cacheOptions.get(optionId);
		if (options == null) {
			synchronized (cacheOptions) {
				if (options == null) {
					options = repository.getMwmOptionItems(optionId)
							.stream()
							.map(i -> new OptionItem(i.getCode(), i.getLabel()))
							.collect(Collectors.toList());
					options.add(0, OptionItem.EMPTY);
					cacheOptions.put(optionId, options);
				}
			}
		}
		return options;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public TableSearchInitResponse init(TableSearchInitRequest req) {
		if (req.tableSearchId == null && isEmpty(req.tableName) && isEmpty(req.tableSearchCode))
			throw new BadRequestException("汎用テーブル検索条件IDが未指定です");
		else if (req.tableSearchId == null) {
			if (isEmpty(req.tableName))
				throw new BadRequestException("汎用テーブル名が未指定です");
			else if (isEmpty(req.corporationCode))
				throw new BadRequestException("企業コードが未指定です");
			else if (isEmpty(req.tableSearchCode))
				throw new BadRequestException("汎用テーブル検索コードが未指定です");
		}

		// 汎用テーブルの定義
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final MwmTableSearchEx ts;
		if (req.tableSearchId != null)
			ts = getMwmTableSearch(req.tableSearchId, localeCode);
		else
			ts = repository.getMwmTableSearch(req.tableName, req.corporationCode, req.tableSearchCode, localeCode);

		List<MwmTableSearchColumnEx> cols = null;
		List<String> primaryKeys = null;
		if (ts != null) {
			cols = getColumns(ts, localeCode);
			primaryKeys = meta.getPrimaryKeys(ts.tableName);
		}
		// バリデーション
		validateInit(req, ts, cols);

		final TableSearchInitResponse res = createResponse(TableSearchInitResponse.class, req);
		res.tableSearchId = req.tableSearchId;
		res.tableName = ts.tableName;
		res.initSearch = eq(CommonFlag.ON, ts.defaultSearchFlag);
		res.conditionDefs = toConditionCols(req.initConditions, cols);
		res.resultDefs = toResultCols(primaryKeys, cols);
		res.defaultSortColumns = toDefaultOrderByColumn(primaryKeys, cols);
		res.defaultSortDirections = toDefaultOrderByDirection(cols);
		res.success = true;
		// 表示名が指定されていれば、画面名称を置き換える
		if (isEmpty(ts.displayName)) {
			res.title += " - " + ts.tableName + " [" + ts.tableSearchName + "]";
		} else {
			final String screenId = toScreenInfo(hsr).getScreenId();
			res.title = screenId + " " + ts.displayName;
		}
		return res;
	}

	/** デフォルトソート用のソート方向 */
	private String toDefaultOrderByDirection(List<MwmTableSearchColumnEx> cols) {
		// 汎用テーブル検索条件カラムに定義されたデフォルトソート方向
		// ソート方向が未設定の場合は空文字を設定する（ソート項目との数合わせのため）
		final List<String> directions = cols.stream()
				.filter(c -> c.resultOrderByPosition != null)
				.sorted((c1, c2) -> compareTo(c1.resultOrderByPosition, c2.resultOrderByPosition))
				.map(c -> defaults(c.resultOrderByDirection, ""))
				.collect(Collectors.toList());
		return directions.stream().collect(Collectors.joining(", "));
	}

	/** デフォルトソート用のソート項目 */
	private String toDefaultOrderByColumn(List<String> primaryKeys, List<MwmTableSearchColumnEx> cols) {
		// 汎用テーブル検索条件カラムに定義されたデフォルトソート項目
		final List<String> sorts = cols.stream()
				.filter(c -> c.resultOrderByPosition != null)
				.sorted((c1, c2) -> compareTo(c1.resultOrderByPosition, c2.resultOrderByPosition))
				.map(c -> c.columnName)
				.collect(Collectors.toList());
		// 確実にページングさせるため、テーブルのPKをユニークキーに含める
		for (String pk : primaryKeys) {
			if (!sorts.contains(pk))
			sorts.add(pk);
		}
		return sorts.stream().collect(Collectors.joining(", "));
	}

	/** 検索結果用の汎用テーブル検索条件カラムを抽出 */
	private List<TableSearchResultDef> toResultCols(List<String> primaryKeys, List<MwmTableSearchColumnEx> cols) {
		final Set<String> keys = new HashSet<>(
				Arrays.asList( ResultDisplayType.DISPLAY, ResultDisplayType.HIDDEN));
		return cols.stream()
				.filter(c -> keys.contains(c.resultDisplayType))
				.map(c -> new TableSearchResultDef(primaryKeys, c))	// 検索条件初期値をここで展開している
				.sorted((c1, c2) -> compareTo(c1.displayPosition, c2.displayPosition))
				.collect(Collectors.toList());
	}

	/** 検索条件用の汎用テーブル検索条件カラムを抽出 */
	private List<TableSearchConditionDef> toConditionCols(Map<String, String> initConditions, List<MwmTableSearchColumnEx> cols) {
		final Set<String> keys = new HashSet<>(
				Arrays.asList( ConditionDisplayType.DISPLAY_TEXTBOX, ConditionDisplayType.DISPLAY_DROPDOWN, ConditionDisplayType.HIDDEN));
		return cols.stream()
				.filter(c -> keys.contains(c.conditionDisplayType))
				.map(c -> createConditionDef(initConditions, c))	// 検索条件初期値をここで展開している
				.sorted((c1, c2) -> compareTo(c1.displayPosition, c2.displayPosition))
				.collect(Collectors.toList());
	}

	/** 検索条件を生成 */
	private TableSearchConditionDef createConditionDef(Map<String, String> initConditions, MwmTableSearchColumnEx src) {
		// 型変換
		final TableSearchConditionDef c = new TableSearchConditionDef(src);

		// 検索条件初期値１を展開
		final String initType1 = c.initType1;
		final String initValue1 = c.initValue1;
		if (isNotEmpty(initType1)) {
			c.initValue1 = toInitValue(initConditions, c.columnName, initType1, initValue1);
		}
		// 検索条件初期値２を展開
		final String initType2 = c.initType2;
		if (isNotEmpty(initType2)) {
			final String initValue2 = c.initValue2;
			c.initValue2 = toInitValue(initConditions, c.columnName, initType2, initValue2);
		}

		// ドロップダウンの選択肢
		if (eq(ConditionDisplayType.DISPLAY_DROPDOWN, c.displayType) && src.conditionOptionId != null) {
			c.options = getMwmOptionItems(src.conditionOptionId);
			if (isNotEmpty(initType1)) {
				c.initValue1 = toInitValue(initConditions, c.columnName, initType1, initValue1);
			}
		}
		return c;
	}

	/** 検索条件初期値を展開（検索条件の初期値をコンテキストに合わせて設定） */
	private String toInitValue(Map<String, String> params, String colName, String initType, String initValue) {
		switch (initType) {
		case ConditionInitType.BLANK:
			return "";
		case ConditionInitType.LITERAL:
			return initValue;
		case ConditionInitType.CORPORATION_CODE:
			return sessionHolder.getLoginInfo().getCorporationCode();
		case ConditionInitType.USER_ADDED_INFO:
			return sessionHolder.getLoginInfo().getUserAddedInfo();
		case ConditionInitType.LOCALE_CODE:
			return sessionHolder.getLoginInfo().getLocaleCode();
		case ConditionInitType.SYSDATE:
			return toStr(today(), "yyyy/MM/dd");
		case ConditionInitType.PARTS:
			return params.get(colName);
		}
		return "";
	}

	/** 初期化用バリデーション */
	private void validateInit(TableSearchInitRequest req, MwmTableSearchEx ts, List<MwmTableSearchColumnEx> cols) {
		// 汎用テーブル検索条件は必須
		if (ts == null)
			throw new NotFoundException(String.format(
					"汎用テーブル検索条件がありません --> tableSearchId=%d tableName=%s corporationCode=%s tableSearchCode=%s"
					, req.tableSearchId, req.tableName, req.corporationCode, req.tableSearchCode));
		// カラムが必須
		if (cols.isEmpty())
			throw new NotFoundException(String.format(
					"汎用テーブル検索条件カラムがありません --> tableSearchId=%d tableName=%s corporationCode=%s tableSearchCode=%s"
					, req.tableSearchId, req.tableName, req.corporationCode, req.tableSearchCode));
	}

	/**
	 * 検索（主にポップアップ画面からUIを伴って呼び出される）
	 * @param req
	 * @return
	 */
	public TableSearchResponse search(TableSearchRequest req) {
		final int allCount = repository.count(req);
		final TableSearchResponse res = createResponse(TableSearchResponse.class, req, allCount);
		res.results = repository.select(req, allCount);
		res.success = true;
		return res;
	}

	/** 汎用テーブル検索条件ID＋検索条件を文字列表現化 */
	private String toKey(Long tableSearchId, Map<String, String> initConditions) {
		StringBuilder sb = new StringBuilder(128)
				.append(tableSearchId)
				.append("@{");
		for (String key : initConditions.keySet()) {
			String val = initConditions.get(key);
			sb.append("[").append(key).append("=").append(val).append("]");
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 検索（主にパーツのレンダラーから呼び出される）
	 * @param tableSearchId 汎用テーブル検索条件ID
	 * @param initConditions 初期値Map（カラム名がキー）
	 * @return
	 */
	public List<TableSearchEntity> search(Long tableSearchId, Map<String, String> initConditions) {
		if (tableSearchId == null)
			return new ArrayList<>();

		// キャッシュにあるか？
		String key = toKey(tableSearchId, initConditions);
		List<TableSearchEntity> results = cacheSearchResults.get(key);
		if (results != null) {
			return results;
		}

		// 汎用テーブル関連のマスタを抽出
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final MwmTableSearchEx ts = getMwmTableSearch(tableSearchId, localeCode);
		final List<MwmTableSearchColumnEx> cols = getColumns(ts, localeCode);
		final List<String> primaryKeys = meta.getPrimaryKeys(ts.tableName);

		if (ts == null || cols == null || cols.isEmpty())
			return new ArrayList<>();

		// 検索用のダミーリクエストを生成
		// ※ページ制御されないよう、pageSize/pageNoは指定しない
		final TableSearchRequest dummy = new TableSearchRequest();
		dummy.conditionDefs = toConditionCols(initConditions, cols);
		dummy.resultDefs = toResultCols(primaryKeys, cols);
		dummy.defaultSortColumns = toDefaultOrderByColumn(primaryKeys, cols);
		dummy.defaultSortDirections = toDefaultOrderByDirection(cols);
		dummy.tableName = ts.tableName;
		dummy.tableSearchId = tableSearchId;

		// 検索結果カラムがないなら検索してもしょうがない
		if (dummy.resultDefs.isEmpty())
			return new ArrayList<>();

		// カラム定義とパーツ入力値をマージして、検索条件値とする。
		// conditionDefs側は生成時点でパーツ入力値とマージして initValueを設定済みなので、こちらをつかう
		for (TableSearchConditionDef c : dummy.conditionDefs) {
			if (eq(ConditionMatchType.RANGE, c.matchType)) {
				initConditions.put(c.columnName + "_FROM", c.initValue1);
				initConditions.put(c.columnName + "_TO", c.initValue2);
			} else {
				initConditions.put(c.columnName, c.initValue1);
			}
		}
		dummy.conditions = initConditions;

		// 検索実行
		results = repository.select(dummy, Integer.MAX_VALUE);

		// キャッシュへ
		cacheSearchResults.put(key, results);

		return results;
	}
}
