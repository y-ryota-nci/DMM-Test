package jp.co.nci.iwf.designer.service.userData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.service.tableInfo.ColumnMetaData;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaData;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * コンテナ・テーブル定義とDBとの同期サービス
 */
@BizLogic
public class UserTableSyncService extends BaseService {
	/** DB接続先サービス */
	@Inject DestinationDatabaseService destDbService;

	/**
	 * CDIが管理しているConnection（RequestScope）。
	 * CDI管理なので勝手にクローズしてはダメ。
	 */
	@Inject
	private Connection conn;
	@Inject
	private Logger log;
	@Inject
	private TableMetaDataService meta;

	/**
	 * カラム定義とDBのテーブル定義を同期。
	 * 　カラム定義リストにあってテーブルにないフィールドはカラム追加。
	 * 　カラム定義リストにもテーブルにもあるが、差異があるならカラム修正。
	 * 　カラム定義になくテーブルにあるフィールドはカラムを削除（削除しないことも可能）。
	 * @param containerName コンテナ名
	 * @param tableName テーブル名
	 * @param columns カラム定義リスト
	 */
	@Transactional
	public List<String> syncTable(MwmContainer c, List<PartsColumn> columns) {
		final String containerName = c.getContainerName();
		final String tableName =  c.getTableName();
		final List<String> errors = new ArrayList<>();

		// 処理前のテーブル定義をログ出力
		if (log.isDebugEnabled())
			debugPrint("処理前", tableName);

		// テーブルがまだない
		if (!meta.isExistTable(tableName)) {
			createTable(containerName, tableName, columns);
		}
		// テーブルはあるけれどレコードなしで、かつDB同期時のテーブル削除可のとき
		else if (!meta.isExistRecord(tableName) && !eq(c.getNotDropTableFlag(), CommonFlag.ON)) {
			dropTable(tableName);
			createTable(containerName, tableName, columns);
		}
		// テーブルがあってレコードも存在する、またはレコードなしでDB同期時のテーブル削除不可のとき
		else {
			// テーブル定義を抽出
			final TableMetaData tbl = meta.getTable(tableName);
			if (!eq(containerName, tbl.comment)) {
				// テーブルコメントの変更
				updateTblCommment(tableName, containerName);
			}

			// テーブルのカラム定義を抽出
			final Map<String, PartsColumn> tcols = getTableColumns(tableName);
			for (PartsColumn pc : columns) {
				// 差分を求めて、反映。このときバリデーションはしない。
				// なぜならDBMS間の差異が多すぎてパターンを網羅できないのが
				// 自明であるからだ。
				// DBMSにDDLを発行して失敗をキャッチしたほうが建設的である
				final PartsColumn tc = tcols.remove(pc.columnName);
				if (tc == null) {
					// 追加
					final String msg = appendColumn(tableName, pc);
					if (isNotEmpty(msg)) errors.add(msg);
				}
				else if (!eq(pc.columnTypeLabel, tc.columnTypeLabel)) {
					// 変更
					final String msg = modifyColumn(tableName, pc);
					if (isNotEmpty(msg)) errors.add(msg);
				}
				else if (!eq(pc.comments, tc.comments)) {
					// カラムコメントの変更
					updateColCommment(tableName, pc);
				}
			}
//			// 未使用になったカラムは削除、デザイナーで必要なカラムは除く
//			for (PartsColumn tc : tcols.values()) {
//				if (!systemColumns.contains(tc.columnName)) {
//					final String msg = dropColumn(tableName, tc);
//					if (isNotEmpty(msg)) errors.add(msg);
//				}
//			}
		}

		// 処理後のテーブル定義をログ出力
		if (log.isDebugEnabled())
			debugPrint("処理後", tableName);

		return errors;
	}

	/** テーブル定義内容をログ出力 */
	private void debugPrint(String str, String tableName) {
		TableMetaData t = meta.getTable(tableName);
		if (t == null) {
			log.debug("{} は存在せず", tableName);
			return;
		}
		log.debug("{} ------------------------------", str);
		log.debug("{} {} {", t.comment, t.tableName);

		List<ColumnMetaData> cols = meta.getColumns(tableName);
		if (cols == null || cols.isEmpty()) {
			log.debug("  {}にカラムなし", tableName);
			return;
		}
		for (ColumnMetaData c : cols) {
			log.debug("  {} {} {}({})", c.comment, c.columnName, c.columnType, c.columnSize, c.decimalPoint);
		}
		log.debug("}");
	}

	/** テーブルを物理削除 */
	private void dropTable(String tableName) {
		execTableSql("drop table " + tableName);
	}

	/** テーブル作成 */
	private void createTable(String containerName, String tableName, List<PartsColumn> columns) {
		// CREATE TABLE
		{
			final HtmlStringBuilder s = new HtmlStringBuilder(512);
			s.append("create table ").append(tableName).append("(");
			s.appendFormat(	  "%s NUMBER(18, 0) not null", UserTable.RUNTIME_ID);
			s.appendFormat(	", %s VARCHAR2(10) not null", UserTable.CORPORATION_CODE);
			s.appendFormat(	", %s NUMBER(10, 0) not null", UserTable.PROCESS_ID);
			s.appendFormat(	", %s NUMBER(18, 0) default -1 not null", UserTable.PARENT_RUNTIME_ID);
			s.appendFormat(	", %s NUMBER(4, 0) default 1 not null", UserTable.SORT_ORDER);
			s.appendFormat(	", %s VARCHAR2(1) default '0' not null", UserTable.DELETE_FLAG);
			s.appendFormat(	", %s NUMBER(18, 0) not null", UserTable.VERSION);
			s.appendFormat(	", %s VARCHAR2(10) default '-' not null", UserTable.CORPORATION_CODE_CREATED);
			s.appendFormat(	", %s VARCHAR2(25) default '-' not null", UserTable.USER_CODE_CREATED);
			s.appendFormat(	", %s TIMESTAMP(3) default systimestamp not null", UserTable.TIMESTAMP_CREATED);
			s.appendFormat(	", %s VARCHAR2(10) default '-' not null", UserTable.CORPORATION_CODE_UPDATED);
			s.appendFormat(	", %s VARCHAR2(25) default '-' not null", UserTable.USER_CODE_UPDATED);
			s.appendFormat(	", %s TIMESTAMP(3) default systimestamp not null", UserTable.TIMESTAMP_UPDATED);

			for (PartsColumn pc : columns) {
				s.append(", ").append(toField(pc));
			}
			s.appendFormat(", constraint %s_PKC primary key (%s)", tableName, UserTable.RUNTIME_ID);
			s.append(")");
			execTableSql(s.toString());
		}
		// INDEX（プロセスID）
		{
			execTableSql(String.format("create index %s_IX1 on %s(%s, %s)"
					, tableName, tableName, UserTable.PROCESS_ID, UserTable.CORPORATION_CODE));
		}
		// INDEX（親ID）
		{
			execTableSql(String.format("create index %s_IX2 on %s(%s, %s)"
					, tableName, tableName, UserTable.PARENT_RUNTIME_ID, UserTable.SORT_ORDER));
		}

		// コメント
		{
			final List<String> comments = new ArrayList<>();
			comments.add(String.format("comment on table %s is '%s'", tableName, escape(containerName)));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.RUNTIME_ID, "ランタイムID"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.CORPORATION_CODE, "企業コード"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.PROCESS_ID, "プロセスID"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.PARENT_RUNTIME_ID, "親ランタイムID"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.SORT_ORDER, "並び順"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.DELETE_FLAG, "削除区分"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.VERSION, "バージョン"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.CORPORATION_CODE_CREATED, "登録会社CD"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.USER_CODE_CREATED, "登録者"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.TIMESTAMP_CREATED, "登録日時"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.CORPORATION_CODE_UPDATED, "更新会社CD"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.USER_CODE_UPDATED, "更新者"));
			comments.add(String.format("comment on column %s.%s is '%s'", tableName, UserTable.TIMESTAMP_UPDATED, "更新日時"));


			for (PartsColumn pc : columns) {
				comments.add(String.format("comment on column %s.%s is '%s'", tableName, pc.columnName, escape(pc.comments)));
			}
			for (String sql : comments) {
				execTableSql(sql);
			}
		}
	}

	/** カラムの削除 */
	@SuppressWarnings("unused")
	private String dropColumn(String tableName, PartsColumn pc) {
		return execColumnSql(String.format("alter table %s drop column %s", tableName, pc.columnName), tableName, pc);
	}

	/**
	 * カラムの修正 */
	private String modifyColumn(String tableName, PartsColumn pc) {
		// たぶん様々な例外が発生するだろうが、それを事前にチェックせずDBMSに任せる。
		// DB製品ごと、バージョンごとに差がありすぎて、とても対応していられないよ
		String msg = execColumnSql(String.format("alter table %s modify %s", tableName, toField(pc)), tableName, pc);
		updateColCommment(tableName, pc);
		return msg;
	}

	/** カラム追加 */
	private String appendColumn(String tableName, PartsColumn pc) {
		String msg = execColumnSql(String.format("alter table %s add %s", tableName, toField(pc)), tableName, pc);
		updateColCommment(tableName, pc);
		return msg;
	}

	/** カラム・コメントの更新 */
	private void updateColCommment(String tableName, PartsColumn pc) {
		execTableSql(String.format("comment on column %s.%s is '%s'", tableName, pc.columnName, escape(pc.comments)));
	}

	/** テーブル・コメントの更新 */
	private void updateTblCommment(String tableName, String containerName) {
		execTableSql(String.format("comment on table %s is '%s'", tableName, escape(containerName)));
	}

	/**
	 * カラム定義を ALTER文などで使用できるSQLのフィールド定義文字列にして返す
	 * @param pc
	 * @return
	 */
	private String toField(PartsColumn pc) {
		switch (pc.columnType) {
		case ColumnType.VARCHAR:
			return String.format("%s VARCHAR2(%d)", pc.columnName, pc.columnSize);
		case ColumnType.NUMBER:
			return String.format("%s NUMBER(%d, %d)", pc.columnName, pc.columnSize, defaults(pc.decimalPoint, 0) );
		case ColumnType.DATE:
			return String.format("%s DATE", pc.columnName);
		case ColumnType.TIMESTAMP:
			return String.format("%s TIMESTAMP(3)", pc.columnName);
		case ColumnType.CLOB:
			return String.format("%s CLOB", pc.columnName);
		case ColumnType.BLOB:
			return String.format("%s BLOB", pc.columnName);
		}
		return String.format("%s UNKNOWN(columnType=%d)", pc.columnName, pc.columnType);
	}

	/**
	 * カラム定義情報を抽出し、カラム名をキーにMap化
	 * @param tableName テーブル名
	 * @return カラム名をキーとしたカラム定義Map
	 */
	private Map<String, PartsColumn> getTableColumns(String tableName) {
		try (ResultSet rs = getColumnMetaData(tableName)) {
			final Map<String, PartsColumn> map = new HashMap<>();
			while (rs.next()) {
				final PartsColumn pc = new PartsColumn();
				pc.columnName = rs.getString("COLUMN_NAME");
				pc.columnType = toColumnType(rs.getString("TYPE_NAME"));
				pc.columnSize = rs.getInt("COLUMN_SIZE");
				pc.decimalPoint = toDecimalPoint(rs.getObject("DECIMAL_DIGITS"));
				pc.columnTypeLabel = pc.toString();
				map.put(pc.columnName, pc);
			}
			return map;
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 小数点桁数 */
	private Integer toDecimalPoint(Object obj) {
		if (obj instanceof Number) {
			return ((Number)obj).intValue();
		}
		return null;
	}

	/** DBのカラム型を MWM_PARTS_COLUMN.COLUMN_TYPEの定数として返す */
	private Integer toColumnType(String typeName) {
		if (isNotEmpty(typeName)) {
			return meta.toColumnType(typeName);
		}
		return -1;
	}

	/** カラムのメタ情報をResultSetとして返す */
	private ResultSet getColumnMetaData(String tableName) throws SQLException {
		final String schema = destDbService.getUser();
		final DatabaseMetaData dmd = conn.getMetaData();
		return dmd.getColumns(null, schema, tableName, "%");
	}

	/**
	 * TABLE用のSQL(CREATE / DROP文とか)を実行する。
	 * 例外はそのままスロー。
	 * @param sql SQL
	 */
	private void execTableSql(String sql) {
		try {
			NativeSqlUtils.execSql(conn, sql);
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * テーブルのカラム用SQL(alter table ZZZ modify...とか)を実行する。
	 * 例外はスローせず、すべてエラーメッセージにして返す。エラーが無ければNULLを返す
	 * @param sql SQL
	 * @param tableName テーブル名
	 * @param pc カラム定義
	 * @return エラーメッセージ。エラーが無ければNULLを返す
	 */
	private String execColumnSql(String sql, String tableName, PartsColumn pc) {
		try {
			NativeSqlUtils.execSql(conn, sql);
			return null;
		}
		catch (SQLException e) {
			SQLException ex = defaults(e.getNextException(), e);
			String msg = String.format("%s.%s(%s) : %s", tableName, pc.columnName, pc.comments, ex.getMessage());
			log.error(msg, e);
			return msg;
		}
	}

	/** SQLエスケープ */
	private static String escape(String s) {
		if (s == null)
			return null;
		return s.replaceAll("'", "''");
	}
}
