package jp.co.nci.iwf.component.tableSearch;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.exception.InvalidColumnDefinisionException;
import jp.co.nci.iwf.jersey.exception.InvalidTableDefinisionException;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 汎用テーブルのリポジトリ
 */
@ApplicationScoped
public class TableSearchRepository extends BaseRepository {
	/**
	 * CDIが管理しているConnection（RequestScope）。
	 * CDI管理なので勝手にクローズしてはダメ。
	 */
	@Inject private Connection conn;

	/** 汎用テーブルマスタを抽出 */
	public MwmTableEx getMwmTable(long tableId) {
		final LoginInfo login = LoginInfo.get();
		final Object[] params = { login.getLocaleCode(), login.getCorporationCode(), tableId };
		MwmTableEx entity = selectOne(MwmTableEx.class, getSql("TI0040_01"), params);
		if (entity != null)
			em.detach(entity);
		return entity;
	}

	/** 汎用テーブル権限ビューの該当件数を返す */
	public int countMwvTableAuthority(long tableId) {
		final LoginInfo login = LoginInfo.get();
		final StringBuilder sql = new StringBuilder(getSql("TI0040_03"));
		final List<Object> params = new ArrayList<>();
		params.add(tableId);

		// メニューロールコード
		sql.append(toInListSql(" MENU_ROLE_CODE", login.getMenuRoleCodes().size()));
		params.addAll(login.getMenuRoleCodes());

		return count(sql.toString(), params.toArray());
	}

	/** 汎用テーブル検索条件を1レコード抽出 */
	public MwmTableSearchEx getMwmTableSearch(Long tableSearchId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("TI0051_01"));
		sql.append(" S.TABLE_SEARCH_ID = ?");
		final Object[] params = { localeCode, tableSearchId };
		return selectOne(MwmTableSearchEx.class, sql.toString(), params);
	}

	/** 汎用テーブル名＋汎用テーブル検索条件コードをキーに汎用テーブル情報を抽出 */
	public MwmTableSearchEx getMwmTableSearch(String tableName, String corporationCode, String tableSearchCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("TI0051_01"));
		sql.append(" T.TABLE_NAME = ? and S.CORPORATION_CODE = ? and S.TABLE_SEARCH_CODE = ?");
		final Object[] params = { localeCode, tableName, corporationCode, tableSearchCode };
		return selectOne(MwmTableSearchEx.class, sql.toString(), params);
	}

	/** 汎用テーブル検索条件カラムを抽出 */
	public List<MwmTableSearchColumnEx> getColumns(Long tableSearchId, String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode, tableSearchId };
		return select(MwmTableSearchColumnEx.class, getSql("TI0051_02"), params);
	}

	/** 汎用テーブル検索画面.件数の抽出 */
	public int count(TableSearchRequest req) {
		final StringBuilder sql = new StringBuilder(256);
		final List<Object> params = new ArrayList<>();

		// 共通絞り込み条件をセット
		fillCondition(req, sql, params, false);

		// SQL発行
		return count(sql.toString(), params.toArray());
	}

	/** 汎用テーブル検索画面.結果行の抽出 */
	public List<TableSearchEntity> select(TableSearchRequest req, int allCount) {
		if (allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(256);
		final List<Object> params = new ArrayList<>();

		// 共通絞り込み条件をセット
		fillCondition(req, sql, params, true);

		try {
			// SQL発行
			final List<Map<String, Object>> srcList =
					NativeSqlUtils.select(conn, sql.toString(), params.toArray());

			// 検索結果の詰め替え
			final List<TableSearchEntity> results = new ArrayList<>(srcList.size());
			for (Map<String, Object> src : srcList) {
				TableSearchEntity row = new TableSearchEntity();
				for (TableSearchResultDef r : req.resultDefs) {
					final String val = toValue(src.get(r.columnName), r.columnType);
					row.put(r.columnName, val);
				}
				results.add(row);
			}
			return results;
		}
		catch (SQLException e) {
			final Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// テーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0202, req.tableName, req.tableSearchId);
			}
			if (next instanceof SQLDataException) {
				// 入力値の型とカラム型が合ってない
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0202, req.tableName, req.tableSearchId);
			}
			throw new InternalServerErrorException(e);
		}
	}

	/** DBの抽出値を正規化 */
	private String toValue(Object obj, String columnType) {
		String value = null;
		if (obj == null) {
			value = null;
		}
		else if (eq(SearchColumnType.STRING, columnType)) {
			value = (obj == null ? null : obj.toString());
		}
		else if (eq(SearchColumnType.NUMBER, columnType)) {
			value = (obj == null ? null : obj.toString());
		}
		else if (eq(SearchColumnType.DATE, columnType)) {
			if (obj instanceof Date)
				value = toStr((Date)obj, "yyyy/MM/dd");
			else
				value = toStr(obj);
		}
		else if (eq(SearchColumnType.TIMESTAMP, columnType)) {
			if (obj instanceof Date)
				value = toStrTimestamp((Date)obj);
			else
				value = toStr(obj);
		}
		else {
			value = toStr(obj);
		}
		return value;
	}

	private static Object parseString(String val, String columnType) {
		if (val == null)
			return null;
		switch (columnType) {
		case SearchColumnType.STRING:
			return val;
		case SearchColumnType.NUMBER:
			return toBD(val);
		case SearchColumnType.DATE:
			return toDate(val, "yyyy/MM/dd");
		case SearchColumnType.TIMESTAMP:
			return new Timestamp(toDate(val, "yyyy/MM/dd").getTime());
		}
		return val;
	}

	/** 汎用テーブル検索画面の共通処理 */
	private void fillCondition(TableSearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		// SELECT句
		sql.append("select /* TI0000.tableSearchId=").append(req.tableSearchId).append(" */ ");
		if (paging) {
			if (req.resultDefs.isEmpty())
				throw new InternalServerErrorException("汎用テーブルの検索結果定義がありません。tableSearchId=" + req.tableSearchId);
			for (int i = 0; i < req.resultDefs.size(); i++) {
				sql.append(i == 0 ? "" : ", ");
				sql.append(req.resultDefs.get(i).columnName);
			}
		}
		else {
			sql.append("count(*)");
		}

		// FROM句
		sql.append(" from ").append(req.tableName);

		// WHERE句
		if (!req.conditionDefs.isEmpty()) {
			WhereBuilder where = new WhereBuilder();
			for (int i = 0; i < req.conditionDefs.size(); i++) {
				final TableSearchConditionDef c = req.conditionDefs.get(i);
				final String value = modifyValue(req.conditions.get(c.columnName), c);

				// 範囲
				if (eq(ConditionMatchType.RANGE, c.matchType)) {
					String from = modifyValue(req.conditions.get(c.columnName + "_FROM"), c);
					String to = modifyValue(req.conditions.get(c.columnName + "_TO"), c);
					if (isEmpty(from) && isEmpty(to)) {
						if (c.required)
							// 必須入力
							throw new BadRequestException("汎用テーブル検索条件ID=" + req.tableSearchId + "のカラム" + c.columnName + "は検索条件が必須ですが、入力されていません");
						else if (c.searchAsBlank)
							// NULLで検索
							if (eq(ConditionMatchType.NOT_EQUAL, c.matchType))
								where.isNotNull(c);
							else
								where.isNull(c);
						else if (eq(ConditionBlankType.FORCE_RECORD_ZERO, c.blankType))
							// 検索結果0件を強制
							where.forceZeroRecord();
					}
					// between ? and ?
					else if (isNotEmpty(from) && isNotEmpty(to)) {
						where.between(c);
						params.add(parseString(from, c.columnType));
						params.add(parseString(to, c.columnType));
					}
					// 以上（columnName >= ?）
					else if (isNotEmpty(from)) {
						where.gte(c);
						params.add(parseString(from, c.columnType));
					}
					// 以下（columnName <= ?）
					else if (isNotEmpty(to)) {
						where.lte(c);
						params.add(parseString(to, c.columnType));
					}
				}
				// 値が未指定
				else if (isEmpty(value)) {
					if (c.required)
						// 必須入力
						throw new BadRequestException("汎用テーブル検索条件ID=" + req.tableSearchId + "のカラム" + c.columnName + "は検索条件が必須ですが、入力されていません");
					else if (c.searchAsBlank)
						// NULLで検索
						if (eq(ConditionMatchType.NOT_EQUAL, c.matchType))
							where.isNotNull(c);
						else
							where.isNull(c);
					else if (eq(ConditionBlankType.FORCE_RECORD_ZERO, c.blankType))
						// 検索結果0件を強制
						where.forceZeroRecord();
				}
				// 完全一致
				else if (eq(c.matchType, ConditionMatchType.FULL)) {
					where.eq(c);
					params.add(parseString(value, c.columnType));
				}
				// 前方一致
				else if (eq(c.matchType, ConditionMatchType.FRONT)) {
					where.likeForwardMatch(c);
					params.add(escapeLikeFront(value));
				}
				// 部分一致
				else if (eq(c.matchType, ConditionMatchType.BOTH)) {
					where.likePartialMatch(c);
					params.add(escapeLikeBoth(value));
				}
				// 不等価
				else if (eq(c.matchType, ConditionMatchType.NOT_EQUAL)) {
					where.notEq(c);
					params.add(parseString(value, c.columnType));
				}
				// 超過（ columnName > ?）
				else if (eq(c.matchType, ConditionMatchType.GT)) {
					where.gt(c);
					params.add(parseString(value, c.columnType));
				}
				// 以上（ columnName >= ?）
				else if (eq(c.matchType, ConditionMatchType.GTE)) {
					where.gte(c);
					params.add(parseString(value, c.columnType));
				}
				// 未満（ columnName < ?）
				else if (eq(c.matchType, ConditionMatchType.LT)) {
					where.lt(c);
					params.add(parseString(value, c.columnType));
				}
				// 以下（columnName <= ?）
				else if (eq(c.matchType, ConditionMatchType.LTE)) {
					where.lte(c);
					params.add(parseString(value, c.columnType));
				}
			}
			sql.append(where.toString());
		}

		// ソート
		if (paging) {
			// OrderBy句を追加
			sql.append(createOrderBy(req));

			// ページング
			if (req.pageNo != null && req.pageSize != null) {
				sql.append(" offset ? rows fetch first ? rows only");
				params.add(toStartPosition(req.pageNo, req.pageSize));
				params.add(req.pageSize);
			}
		}
	}

	/** 検索条件カラムの定義に従って、値を補正して返す */
	private String modifyValue(String s, TableSearchConditionDef c) {
		String val = (c.conditionTrimFlag ? trim(s) : s);
		return val;
	}

	/** 選択肢項目マスタを抽出 */
	public List<MwmOptionItem> getMwmOptionItems(Long optionId) {
		final Object[] params = { LoginInfo.get().getLocaleCode(), optionId };
		return select(MwmOptionItem.class, getSql("TI0000_03"), params);
	}

	/** OrderBy句の生成 */
	private String createOrderBy(TableSearchRequest req) {
		final StringBuilder orderBy = new StringBuilder(64);

		// ソート項目がある場合のみ以下の処理を行う
		if (isNotEmpty(req.sortColumn) || isNotEmpty(req.defaultSortColumns)) {
			orderBy.append(" order by ");

			// 画面にて選択されたソート順は必ず最初のソート項目として設定
			if (isNotEmpty(req.sortColumn)) {
				orderBy.append(req.sortColumn).append(req.sortAsc ? " ASC" : " DESC");
			}

			// 次にデフォルトソート順を設定
			if (isNotEmpty(req.defaultSortColumns)) {
				// カンマ区切りでソートカラム、ソート方向を分割
				String[] cols = req.defaultSortColumns.split(",\\s*");
				String[] dirs = req.defaultSortDirections.split(",\\s*");
				for (int i = 0; i < cols.length; i++){
					// 画面で設定したソート項目と異なる場合のみソート順を設定（つまり同じならスキップ）
					if (isNotEmpty(cols[i]) && !eq(cols[i], req.sortColumn)) {
						orderBy.append(i == 0 && isEmpty(req.sortColumn) ? "" : ", ");
						orderBy.append(cols[i]);
						// ソート方向は"A"、"D"という設定値になっているので値に応じて「ASC／DESC」に変換
						// 設定がない場合は設定しない（結果としてDBの標準のソート方向が使用される、OracleならASC）
						if (dirs.length > i && isNotEmpty(dirs[i])) {
							orderBy.append(eq(dirs[i], ResultOrderByDirection.DESC) ? " DESC" : " ASC");
						}
					}
				}
			}
		}
		return orderBy.toString();
	}
}
