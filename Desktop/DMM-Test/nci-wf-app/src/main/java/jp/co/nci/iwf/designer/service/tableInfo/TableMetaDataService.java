package jp.co.nci.iwf.designer.service.tableInfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * テーブルとカラムのメタデータを扱うサービス
 */
@BizLogic
public class TableMetaDataService extends BaseRepository {
	/** DB接続先情報サービス */
	@Inject private DestinationDatabaseService destination;
	/** CDIが管理しているConnection（RequestScope）。CDI管理なので勝手にクローズしてはダメ。  */
	@Inject private Connection conn;
	/** ロガー */
	@Inject private Logger log;

	private static final String[] TABLE = new String[] { "TABLE" };

	/** DB型：文字列 */
	private static final Set<String> STRINGS = new HashSet<>(Arrays.asList(
			"VARCHAR2", "VARCHAR", "CHAR", "NVARCHAR", "NCHAR"));
	/** DB型：数値 */
	private static final Set<String> NUMERICS = new HashSet<>(Arrays.asList(
			"NUMERIC", "NUMBER", "DECIMAL", "INTEGER", "LONG", "DOUBLE", "FLOAT", "BIGINTEGER"));
	/** DB型：日付 */
	private static final Set<String> DATES = new HashSet<>(Arrays.asList(
			"DATE"));
	/** DB型：日付時刻 */
	private static final Set<String> TIMESTAMPS = new HashSet<>(Arrays.asList(
			"TIMESTAMP", "DATETIME"));
	/** DB型：CLOB */
	private static final Set<String> CLOBS = new HashSet<>(Arrays.asList("CLOB"));
	/** DB型：BLOB */
	private static final Set<String> BLOBS = new HashSet<>(Arrays.asList("BLOB"));

	@Inject private CacheManager cm;
	/** キャッシュ：テーブルPK */
	private CacheHolder<String, List<String>> cachePrimaryKeys;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cachePrimaryKeys = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/** リソース解放 */
	@PreDestroy
	public void dispose() {
		// キャッシュ：テーブルPK
		cm.remove(cachePrimaryKeys);
		cachePrimaryKeys.dispose();
		cachePrimaryKeys = null;
	}

	/**
	 * テーブル／ビューを抽出し、その物理名と論理名を返す
	 * @param tableName テーブル名／ビュー名（部分一致）
	 * @return
	 */
	public List<TableMetaData> getTableAndViews(String... tableNames) {
		// 本当はDatabaseMetaDataとかを使いたいのだが、Oracleはエンティティのコメントを返してくれないので、
		// Oracleのディクショナリへ問合せする
		List<TableMetaData> results = new ArrayList<>(256);
		results.addAll(getTables(tableNames));
		results.addAll(getViews(tableNames));
		return results;
	}

	/**
	 * テーブルを抽出し、その物理名と論理名を返す
	 * @param tableName テーブル名（部分一致）
	 * @return
	 */
	public TableMetaData getTable(String tableNames) {
		List<TableMetaData> list = getTables(tableNames);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * テーブルを抽出し、その物理名と論理名を返す
	 * @param tableNames テーブル名（部分一致）
	 * @return
	 */
	public List<TableMetaData> getTables(String... tableNames) {
		// 本当はDatabaseMetaDataとかを使いたいのだが、Oracleはエンティティのコメントを返してくれないので、
		// Oracleのディクショナリへ問合せする
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder(getSql("CM0015_TABLE"));
		if (tableNames.length > 0) {
			sql.append(" and " + toInListSql("T.TABLE_NAME", tableNames.length));
			for (String name : tableNames)
				params.add(name);
		}
		sql.append(" order by T.TABLE_NAME");
		return select(TableMetaData.class, sql.toString(), params.toArray());
	}

	/**
	 * ビューを抽出し、その物理名と論理名を返す
	 * @param tableName ビュー名（部分一致）
	 * @return
	 */
	public List<TableMetaData> getViews(String... viewNames) {
		// 本当はDatabaseMetaDataとかを使いたいのだが、Oracleはエンティティのコメントを返してくれないので、
		// Oracleのディクショナリへ問合せする
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder(getSql("CM0015_VIEW"));
		if (viewNames.length > 0) {
			sql.append(" and " + toInListSql("T.VIEW_NAME", viewNames.length));
			for (String name : viewNames)
				params.add(name);
		}
		sql.append(" order by T.VIEW_NAME");
		return select(TableMetaData.class, sql.toString(), params.toArray());
	}

	/**
	 * テーブルのカラム定義を抽出
	 * @param tableId
	 * @return
	 */
	public Map<String, ColumnMetaData> getColumnMap(long tableId) {
		MwmTable t = em.find(MwmTable.class, tableId);
		if (t == null)
			return null;
		return getColumns(t.getTableName())
				.stream()
				.collect(Collectors.toMap(c -> c.columnName, c -> c));
	}

	/**
	 * テーブルのカラム定義を抽出
	 * @param tableName
	 * @return
	 */
	public List<ColumnMetaData> getColumns(String tableName) {
		try {
			// プライマリキー（複合キーがあり得る）
			final Set<String> primaryKeys = new HashSet<>(getPrimaryKeys(tableName));

			// コメント
			// 本当は DatabaseMetaDataあたりから取得したいのだが、Oracleはカラムのコメントを返してくれないので
			// Oracleのディクショナリへ問い合わせる
			final List<Map<String, Object>> maps = NativeSqlUtils.select(conn, getSql("CM0016"), tableName);
			final Map<String, String> comments = new HashMap<>();
			for (Map<String, Object> m : maps) {
				String colName = (String)m.get("COLUMN_NAME");
				String comment = (String)m.get("COMMENTS");
				comments.put(colName, comment);
			}
			// カラム
			try (ResultSet rs = getColumnMetaData(tableName)) {
				final List<ColumnMetaData> list = new ArrayList<>();
				int i = 0;
				while (rs.next()) {
					final ColumnMetaData c = new ColumnMetaData();
					c.columnName = rs.getString("COLUMN_NAME");
					String type = rs.getString("TYPE_NAME");
					int pos = type.indexOf('(');
					if (pos > 0) {	// OracleのTIMESTAMP型は TIMESTAMP(6)のような定義となってる場合あり
						c.columnType = type.substring(0, pos);
						String s = type.substring(pos + 1, type.length() - 1);
						c.columnSize = Integer.valueOf(s);
					}
					else {
						c.columnType = type;
						c.columnSize = toInt(rs.getObject("COLUMN_SIZE"));
					}
					c.decimalPoint = toInt(rs.getObject("DECIMAL_DIGITS"));
					c.isNotNull = !toBool(rs.getString("IS_NULLABLE"));
					c.isPrimaryKey = primaryKeys.contains(c.columnName);
					c.comment = (String)comments.get(c.columnName);
					c.sortOrder = ++i;
					c.schema = rs.getString("TABLE_SCHEM");

					// 型による桁数の補正処理
					if (eq("NUMBER", c.columnType)) {
						if (c.columnSize == 0 && c.decimalPoint < 0) {
							c.columnSize = null;	// Oracle で NUMBER型に精度を指定しないと桁数が0、小数点桁数が -127となる
							c.decimalPoint = null;
						}
					}
					else if (eq("DATE", c.columnType)) {
						c.columnSize = null;
						c.decimalPoint = null;
					}
					else if (eq("TIMESTAMP", c.columnType)) {
						c.decimalPoint = null;
					}

					list.add(c);

					log.debug("{}.{} {}({}) --> schema:{}", tableName, c.columnName, c.columnType, c.columnSize, c.schema);
				}
				return list;
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}


	/**
	 * テーブルが存在するか(ビュー名を指定しても存在しないものとして返す)
	 * @param tableName テーブル名
	 * @return
	 */
	public boolean isExistTable(String tableName) {
		try (ResultSet rs = getTableMetaData(tableName)) {
			 if (rs.next()) {
				 return rs.getString("TABLE_NAME").equals(tableName);
			 }
			 return false;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * テーブルのカラム名一覧を返す
	 * @param tableName テーブル名
	 * @return
	 */
	public Set<String> getColNames(String tableName) {
		final Set<String> names = new LinkedHashSet<>(64);
		try (ResultSet rs = getColumnMetaData(tableName)) {
			while (rs.next()) {
				names.add(rs.getString("COLUMN_NAME"));
			}
			return names;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * テーブルにレコードが存在するか。
	 * 【テーブルの存在確認は行われないので注意せよ】
	 * @param tableName テーブル名
	 * @return レコードがあれば true
	 */
	public boolean isExistRecord(String tableName) {
		String sql = "select count(*) from " + tableName;
		try {
			return NativeSqlUtils.count(conn, sql) > 0;
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * テーブルに該当カラムリスト内のカラムが１つでも存在するか。
	 * 【テーブルの存在確認は行われないので注意せよ】
	 * @param tableName テーブル名
	 * @return レコードがあれば true
	 */
	public boolean isExistColumn(String tableName, Collection<PartsColumn> pcs) {
		final Set<String> colNames = new HashSet<>();
		try {
			try (ResultSet rs = getColumnMetaData(tableName)) {
				while (rs.next()) {
					colNames.add(rs.getString("COLUMN_NAME"));
				}
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}

		for (PartsColumn pc : pcs) {
			if (colNames.contains(pc.columnName))
				return true;
		}
		return false;
	}

	/** テーブルのメタ情報をResultSetとして返す */
	private ResultSet getTableMetaData(String tableName) throws SQLException {
		DatabaseMetaData dmd = conn.getMetaData();
		return dmd.getTables(null, destination.getUser(), tableName, TABLE);
	}

	/** カラムのメタ情報をResultSetとして返す */
	private ResultSet getColumnMetaData(String tableName) throws SQLException {
		DatabaseMetaData dmd = conn.getMetaData();
		return dmd.getColumns(null, destination.getUser(), tableName, "%");
	}

	/** プライマリキーのカラムリストを返す */
	public List<String> getPrimaryKeys(String tableName) {
		// キャッシュにあるか？
		List<String> primaryKeys = cachePrimaryKeys.get(tableName);
		if (primaryKeys == null) {
			try {
				final DatabaseMetaData dmd = conn.getMetaData();
				try (ResultSet rs = dmd.getPrimaryKeys(null, destination.getUser(), tableName)) {
					primaryKeys = new ArrayList<>();
					while (rs.next()) {
						primaryKeys.add(rs.getString("COLUMN_NAME"));
					}
				}
				cachePrimaryKeys.put(tableName, primaryKeys);
			}
			catch (SQLException e) {
				throw new InternalServerErrorException(e);
			}
		}
		return primaryKeys;
	}

	/**
	 * カラム型を返す
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public int toColumnType(String dbColumnType) {
		String columnType = dbColumnType.toUpperCase();
		int pos = columnType.indexOf('(');
		if (pos > 0)	// oracleは TIMESTAMP(6)のように桁数を含んでしまうことがあるので、補正
			columnType = columnType.substring(0, pos);

		if (isString(columnType))
			return ColumnType.VARCHAR;
		if (isNumeric(columnType))
			return ColumnType.NUMBER;
		if (isDate(columnType))
			return ColumnType.DATE;
		if (isTimestamp(columnType))
			return ColumnType.TIMESTAMP;
		if (isClob(columnType))
			return ColumnType.CLOB;
		if (isBlob(columnType))
			return ColumnType.BLOB;
		throw new InternalServerErrorException("カラム型を解釈出来ません ColumnMetaData.columnType=" + columnType);
	}

	/**
	 * 文字列としてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isString(String dbColumnType) {
		return STRINGS.contains(dbColumnType);
	}

	/**
	 * 数値としてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isNumeric(String dbColumnType) {
		return NUMERICS.contains(dbColumnType);
	}

	/**
	 * 日付としてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isDate(String dbColumnType) {
		return DATES.contains(dbColumnType);
	}

	/**
	 * 日付時刻としてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isTimestamp(String dbColumnType) {
		return TIMESTAMPS.contains(dbColumnType);
	}

	/**
	 * CLOBとしてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isClob(String dbColumnType) {
		return CLOBS.contains(dbColumnType);
	}

	/**
	 * BLOBとしてみなせるDB型か
	 * @param dbColumnType DBカラム型（VARCHAR2とか）
	 * @return
	 */
	public boolean isBlob(String dbColumnType) {
		return BLOBS.contains(dbColumnType);
	}
}
