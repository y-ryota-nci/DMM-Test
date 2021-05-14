package jp.co.nci.iwf.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.enterprise.inject.spi.CDI;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.input.RowHandler;

/**
 * JPAに依存せず、素のSQLを発行するためのユーティリティ
 */
public final class NativeSqlUtils {
	/**
	 * ログ出力内容の最大桁数。
	 */
	private static final int LOG_MAX_LENGTH = 1024 * 8;


	/**
	 * 独立した(=SA, Stand Alone)Connectionを返す。<br>
	 * JPAでは管理されていないのでConnection.close()は自分でする必要がある。<br>
	 * <strong>close()を忘れるとリソースリークが発生するので、扱いには注意すること</strong>
	 * @return
	 */
	public static Connection getConnectionSA() throws SQLException {
		// DataSourceはCDI管理だが、そこから取得するConnectionは自分で管理する
		final DataSource ds = CDI.current().select(DataSource.class).get();
		final long start = System.currentTimeMillis();
		try {
			return ds.getConnection();
		}
		finally {
			long diff = System.currentTimeMillis() - start;
			if (diff > 1000L) {
				new Exception("NativeSqlUtils.getConnectionSA()でConnectionを取得するのに時間が掛かり過ぎています -> " + diff + "msec").printStackTrace();
			}
		}
	}

	/**
	 * JPAに依存せずに、SQLを発行
	 * @param conn 接続コネクション
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param args パラメータの配列
	 * @return SQLの結果で影響を受けた行数
	 */
	public static int execSql(Connection conn, CharSequence sql, Object...args) throws SQLException {
		final QueryRunner qr = new QueryRunner();
		debugSql(sql, args);

		if (args == null || args.length == 0)
			return qr.update(conn, sql.toString());
		else
			return qr.update(conn, sql.toString(), args);
	}

	/**
	 * JPAに依存せずに、SQLを発行
	 * @param conn 接続コネクション
	 * @param resultClass 検索結果の型
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param args パラメータの配列
	 * @return
	 */
	public static <IF, IMPL extends IF> List<IF> select(Connection conn, Class<IMPL> resultClass, CharSequence sql, Object... args) throws SQLException {
		final QueryRunner qr = new QueryRunner();
		final BeanListHandler<IF> rsh = new BeanListHandler<>(resultClass, new NciRowProcessor());
		debugSql(sql, args);

		if (args == null || args.length == 0)
			return qr.query(conn, sql.toString(), rsh);
		else
			return qr.query(conn, sql.toString(), rsh, args);
	}

	/**
	 * JPAに依存せずに、SQLを発行し、結果をカラム名をキーとしたMapリストにして返す。
	 * また、このMapのキーであるカラム名は取得時に大文字・小文字を区別しない（裏で自動的に toLowerCase()を施す）
	 *
	 * @param conn 接続コネクション
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param params パラメータの配列
	 * @return 列をObject[]としたList
	 */
	public static List<Map<String, Object>> select(Connection conn, CharSequence sql, Object... args) throws SQLException {
		final QueryRunner qr = new QueryRunner();
		final MapListHandler rsh = new MapListHandler(new NciRowProcessor());
		debugSql(sql, args);

		if (args == null || args.length == 0)
			return (List<Map<String, Object>>)qr.query(conn, sql.toString(), rsh);
		else
			return (List<Map<String, Object>>)qr.query(conn, sql.toString(), rsh, args);
	}

	/**
	 * JPAに依存せずにSQLを発行し、結果をフェッチするたびにRowHandlerを呼び出す。
	 * また、このMapのキーであるカラム名は取得時に大文字・小文字を区別しない（裏で自動的に toLowerCase()を施す）
	 * @param conn 接続コネクション
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param params パラメータの配列
	 * @param rowHandler 行ハンドラー
	 * @throws Exception
	 */
	public static void select(Connection conn, CharSequence sql, Object[] args, RowHandler<Map<String, Object>> rowHandler) throws Exception {
		debugSql(sql, args);

		final NciRowProcessor converter = new NciRowProcessor();
		final int FORWARD_ONLY = ResultSet.TYPE_FORWARD_ONLY;
		final int READ_ONLY = ResultSet.CONCUR_READ_ONLY;
		try (PreparedStatement ps = conn.prepareStatement(sql.toString(), FORWARD_ONLY, READ_ONLY)) {
			// フェッチサイズを増やす(経験的に100～1000ぐらいで。10000まで増やすと抽出は早いけれどメモリをバカ食いするので)
			ps.setFetchSize(1000);
			// パラメータ
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// SQL発行
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					// 行データをMapへ変換し、行ハンドラーを呼び出す
					final Map<String, Object> map = converter.toMap(rs);
					if (!rowHandler.accept(map)) {
						break;
					}
				}
			};
		}
	}

	/**
	 * JPAに依存せずに、SQLを発行し、結果をカラム名をキーとしたMapリストにして返す。
	 * また、このMapのキーであるカラム名は取得時に大文字・小文字を区別しない（裏で自動的に toLowerCase()を施す）
	 *
	 * @param conn 接続コネクション
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param params パラメータの配列
	 * @return 列をObject[]としたList
	 */
	public static Map<String, Object> selectOne(Connection conn, CharSequence sql, Object...args) throws SQLException {
		List<Map<String, Object>> list = select(conn, sql, args);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * JPAに依存せずに、SQLを発行
	 * @param conn 接続コネクション
	 * @param resultClass 検索結果の型
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param args パラメータの配列
	 * @return
	 */
	public static <IF, IMPL extends IF> IF selectOne(Connection conn, Class<IMPL> resultClass, CharSequence sql, Object... args) throws SQLException {
		List<IF> list = select(conn, resultClass, sql, args);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * JPAに依存せずに、SQLを発行
	 * @param conn 接続コネクション
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param args パラメータの配列
	 * @return
	 */
	public static long count(Connection conn, CharSequence sql, Object... args) throws SQLException {
		final QueryRunner qr = new QueryRunner();
		final ResultSetHandler<Long> rsh = new ScalarHandler<Long>();
		debugSql(sql, args);

		if (args == null || args.length == 0)
			return ((Number)qr.query(conn, sql.toString(), rsh)).longValue();
		else
			return ((Number)qr.query(conn, sql.toString(), rsh, args)).longValue();
	}

	/**
	 * SQLログ出力
	 * @param sql SQL。パラメータは '?'で埋めること
	 * @param params パラメータの配列
	 */
	public static void debugSql(CharSequence sql, Object... params) {
		final Class<?> c = getCallerClass();
		final Logger log = LoggerFactory.getLogger(c);
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder(sql.toString().replaceAll("[\n\r\t ]+", " "));
			if (params == null || params.length == 0) {
				sb.append("  -- params : []");
			}
			else {
				final StringJoiner sj = new StringJoiner(",");
				for (Object p : params) {
					// Eclipse上でデバッグしているときに、あまりに長い文字列をログ出力しようとすると
					// コンソールのバッファがあふれてロックされてしまう。
					// これを回避するため、出力文字列の上限値を定めて、超過分をカットする。
					final String s = (p == null ? "(null)" : p.toString());
					if (s.length() > LOG_MAX_LENGTH)
						sj.add(s.substring(0, LOG_MAX_LENGTH) + "……");
					else
						sj.add(s);
				}
				sb.append("  -- params : [").append(sj).append("]");
			}
			log.debug("  SQL : {}", sb);	// 改行／タブを除去
		}
	}

	/**
	 * SQLパラメータをセット
	 * @param ps ステートメント
	 * @param params パラメータ配列
	 * @throws SQLException
	 */
	public static void setParams(PreparedStatement ps, Object...params) throws SQLException {
		// パラメータのログ出力
		debugParams(params);

		for (int i = 1; i <= params.length; i++) {
			Object v = params[i - 1];
			if (v == null)
				ps.setNull(i, Types.VARCHAR);
			else
				ps.setObject(i, v);
		}
	}

	/**
	 * パラメータをデバッグ出力
	 * @param params パラメータ配列
	 */
	private static void debugParams(Object...params) {
		final Class<?> c = getCallerClass();
		final Logger log = LoggerFactory.getLogger(c);
		log.debug(Arrays.toString(params));
	}

	/** 当クラスを呼び出したクラスを求める */
	private static Class<?> getCallerClass() {
		final String thName = Thread.class.getName();
		final String myName = NativeSqlUtils.class.getName();
		Class<?> c = NativeSqlUtils.class;	// 求まらなかった時の保険
		for (StackTraceElement stack : Thread.currentThread().getStackTrace()) {
			String className = stack.getClassName();
			// 当クラスおよびThreadクラスでもない最初のクラスが呼び元のクラスのはず
			if (!MiscUtils.contains(className, myName, thName)) {
				c = MiscUtils.forName(className);
				if (className.indexOf("$") >= 0) {
					// たぶんProxyクラスなので、元のクラスにしとく
					c = c.getSuperclass();
				}
				break;
			}
		}
		return c;
	}
}
