package jp.co.nci.iwf.designer.service.userData;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jersey.exception.InvalidColumnDefinisionException;
import jp.co.nci.iwf.jersey.exception.InvalidTableDefinisionException;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * パーツのユーザデータのリポジトリ
 */
@ApplicationScoped
public class UserDataRepository extends BaseRepository {
	/** 採番サービス */
	@Inject private NumberingService number;

	/** 画面情報の読み込み */
	public MwvScreen getMwvScreen(long screenId, String localeCode) {
		final Object[] params = { screenId, localeCode };
		return selectOne(MwvScreen.class, getSql("VD0310_10"), params);
	}

	/** 子画面のユーザデータを抽出 */
	public List<Map<String, Object>> getChildUserData(String tableName, Long parentRuntimeId) {
		// 子画面のユーザデータは親画面のランタイムIDをキーに抽出
		if (parentRuntimeId == null)
			return new ArrayList<>();

		final Object[] params = { parentRuntimeId };
		final String sql = getSql("VD0310_09").replaceAll(quotePattern("{TABLE_NAME}"), tableName);
		try {
			return NativeSqlUtils.select(getConnection(), sql, params);
		}
		catch (SQLException e) {
			Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// コンテナのテーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			if (next instanceof SQLDataException) {
				// パーツのバリデーションと実データが合ってないとか、そんなの。
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			throw new InternalServerErrorException(e);
		}
	}

	/** ルートコンテナのユーザデータを抽出 */
	public List<Map<String, Object>> getRootUserData(String tableName, String corporationCode, Long processId) {
		// 子画面のユーザデータはプロセスIDをキーに抽出
		if (isEmpty(corporationCode) || processId == null)
			return new ArrayList<>();

		final Object[] params = { corporationCode, processId };
		final String sql = getSql("VD0310_08").replaceAll(quotePattern("{TABLE_NAME}"), tableName);
		try {
			return NativeSqlUtils.select(getConnection(), sql, params);
		}
		catch (SQLException e) {
			Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// コンテナのテーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			if (next instanceof SQLDataException) {
				// パーツのバリデーションと実データが合ってないとか、そんなの。
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			throw new InternalServerErrorException(e);
		}
	}


	/**
	 * ユーザデータをインサート
	 * @param tblName テーブル名
	 * @param input ユーザデータ
	 */
	public int insert(UserDataEntity input, LoginInfo login) {
		input.values.put(UserTable.RUNTIME_ID, input.runtimeId);
		input.values.put(UserTable.CORPORATION_CODE, input.corporationCode);
		input.values.put(UserTable.PROCESS_ID, input.processId);
		input.values.put(UserTable.PARENT_RUNTIME_ID, input.parentRuntimeId);
		input.values.put(UserTable.SORT_ORDER, input.sortOrder);
		input.values.put(UserTable.VERSION, 1L);
		input.values.put(UserTable.DELETE_FLAG, input.deleteFlag);
		input.values.put(UserTable.CORPORATION_CODE_CREATED, login.getCorporationCode());
		input.values.put(UserTable.USER_CODE_CREATED, login.getUserCode());
		input.values.put(UserTable.TIMESTAMP_CREATED, timestamp());
		input.values.put(UserTable.CORPORATION_CODE_UPDATED, login.getCorporationCode());
		input.values.put(UserTable.USER_CODE_UPDATED, login.getUserCode());
		input.values.put(UserTable.TIMESTAMP_UPDATED, timestamp());

		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder();

		sql.append("insert into ").append(input.tableName).append("(");

		int n = 0;
		for (String colName : input.values.keySet()) {
			if (n++ > 0)
				sql.append(", ");
			sql.append(colName);
		}
		sql.append(") values (");
		n = 0;
		for (String colName : input.values.keySet()) {
			if (n++ > 0)
				sql.append(", ");
			sql.append("?");
			params.add(input.values.get(colName));
		}
		sql.append(")");

		try {
			return NativeSqlUtils.execSql(
					getConnection(), sql.toString(), params.toArray());
		}
		catch (SQLException e) {
			Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// コンテナのテーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0200, input.tableName);
			}
			if (next instanceof SQLDataException) {
				// パーツのバリデーションと実データが合ってないとか、そんなの。
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0200, input.tableName);
			}
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * ユーザデータを１レコード分だけ更新
	 * @param input テーブル名
	 * @param current ユーザデータ
	 */
	public int update(UserDataEntity input, UserDataEntity current, LoginInfo login) {

		final StringBuilder sql = new StringBuilder();
		sql.append("update ").append(input.tableName)
			.append(" set VERSION = VERSION + 1")
			.append(", CORPORATION_CODE_UPDATED = ?")
			.append(", USER_CODE_UPDATED = ?")
			.append(", TIMESTAMP_UPDATED = ?");
		final List<Object> params = new ArrayList<>();
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp());

		// 削除区分も比較対象に含める
		input.values.put(UserTable.DELETE_FLAG, input.deleteFlag);
		current.values.put(UserTable.DELETE_FLAG, current.deleteFlag);

		// 変更点を探す
		int changed = 0;
		for (String colName : input.values.keySet()) {
			if (UserTable.NOT_UPDATABLES.contains(colName)) {
				continue;
			}
			final Object inp = input.values.get(colName);
			final Object cur = current.values.get(colName);
			if (!current.values.containsKey(colName) || !same(inp, cur)) {
				sql.append(", ").append(colName).append(" = ?");
				params.add(inp);
				changed++;
			}
		}

		// 変更されたカラムが１つもなければ更新自体を行わない
		if (changed == 0) {
			return 0;
		}

		sql.append(" where RUNTIME_ID = ? and VERSION = ?");
		params.add(input.runtimeId);
		params.add(input.version);

		try {
			int update = NativeSqlUtils.execSql(
					getConnection(), sql.toString(), params.toArray());
			if (update != 1)
				throw new AlreadyUpdatedException("テーブル=%s  RUNTIME_ID=%d, VERSION=%d",
						input.tableName, input.runtimeId, input.version);
			return update;
		}
		catch (SQLException e) {
			Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// コンテナのテーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0200, input.tableName);
			}
			if (next instanceof SQLDataException) {
				// パーツのバリデーションと実データが合ってないとか、そんなの。
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0200, input.tableName);
			}
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 等価判定
	 * @param input 画面入力値
	 * @param current DB格納値
	 * @return 画面入力値とDB格納値が等価とみなせればtrue
	 */
	private boolean same(Object input, Object current) {
		// 画面は必ず文字列だが、DBは様々な型がある
		if (isEmpty(current) && isEmpty(input)) {
			return true;
		}
		else if (current instanceof String) {
			return eq(input, (String)current);
		}
		else if (current instanceof Number) {
			Double d1 = isEmpty(input) ? null
					: (input instanceof Number) ? ((Number)input).doubleValue()
					: Double.valueOf(input.toString());
			Double d2 = ((Number)current).doubleValue();
			return eq(d1, d2);
		}
		else if (current instanceof Timestamp) {
			Timestamp t1 = isEmpty(input) ? null
					: (input instanceof Timestamp) ? (Timestamp)input
					: (input instanceof Date) ? new Timestamp(((Date)input).getTime())
					: Timestamp.valueOf(input.toString());
			return eq(t1, (Timestamp)current);
		}
		else if (current instanceof Date) {
			Date d1 = isEmpty(input) ? null
					: (input instanceof Date) ? (Date)input
					: toDate(input.toString(), "yyyy/MM/dd");
			return eq(d1, (Date)current);
		}
		return eq(input, current);
	}

	/**
	 * ランタイムIDに紐付くユーザデータを物理削除
	 * @param runtimeId
	 */
	public int deletePhysical(String tableName, Set<Long> runtimeIds) {
		if (runtimeIds == null || runtimeIds.isEmpty())
			return 0;

		final String sql = new StringBuilder()
				.append("delete from ").append(tableName)
				.append(" where ").append(toInListSql(UserTable.RUNTIME_ID, runtimeIds.size()))
				.toString();
		final Object[] params = runtimeIds.toArray();
		try {
			return NativeSqlUtils.execSql(getConnection(), sql, params);
		} catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** プロセスIDに紐付くユーザデータを抽出 */
	public List<UserDataEntity> get(String tableName, String corporationCode, Long processId) {
		final String sql = new StringBuilder()
			.append("select *")
			.append(" from ").append(tableName)
			.append(" where CORPORATION_CODE = ? and PROCESS_ID = ?")
			.append(" order by PARENT_RUNTIME_ID, SORT_ORDER, RUNTIME_ID")
			.toString();
		final Object[] params = { corporationCode, processId };
		try {
			final List<Map<String, Object>> srcList = NativeSqlUtils.select(
					getConnection(), sql, params);
			final List<UserDataEntity> results = new ArrayList<>(srcList.size());
			for (Map<String, Object> src : srcList) {
				final UserDataEntity row = new UserDataEntity();
				row.tableName = tableName;
				row.values = new HashMap<>();
				for (String name : src.keySet()) {
					name = name.toUpperCase();
					Object val = src.get(name);
					switch (name) {
					case UserTable.CORPORATION_CODE:
						row.corporationCode = (String)val; break;
					case UserTable.DELETE_FLAG:
						row.deleteFlag = (String)val; break;
					case UserTable.PARENT_RUNTIME_ID:
						row.parentRuntimeId = toLong(val); break;
					case UserTable.PROCESS_ID:
						row.processId = toLong(val); break;
					case UserTable.RUNTIME_ID:
						row.runtimeId = toLong(val); break;
					case UserTable.SORT_ORDER:
						row.sortOrder = toInt(val); break;
					case UserTable.VERSION:
						row.version = toLong(val); break;
					default:
						row.values.put(name, val);
					}
				}
				results.add(row);
			}
			return results;
		}
		catch (SQLException e) {
			Exception next = e.getNextException();
			if (next instanceof SQLSyntaxErrorException) {
				// コンテナのテーブル／カラム定義と実データベースが異なる
				throw new InvalidTableDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			if (next instanceof SQLDataException) {
				// パーツのバリデーションと実データが合ってないとか、そんなの。
				throw new InvalidColumnDefinisionException(next, MessageCd.MSG0200, tableName);
			}
			throw new InternalServerErrorException(e);
		}
	}

	public Long nextRuntimeId() {
		return number.next("USER_DATA", UserTable.RUNTIME_ID);
	}

	/** ワークフローパーツ添付ファイル情報を抽出 */
	public List<MwtPartsAttachFileWf> getMwtPartsAttachFileWf(Long runtimeId, Long partsId) {
		if (runtimeId == null || partsId == null) {
			return new ArrayList<>();
		}
		final Object[] params = { runtimeId, partsId };
		return select(MwtPartsAttachFileWf.class, getSql("VD0310_11"), params);
	}

	/** ワークフローパーツ添付ファイルをインサート */
	public MwtPartsAttachFileWf insert(MwtPartsAttachFileWf file) {
		long partsAttachFileWfId = number.newPK(MwtPartsAttachFileWf.class);
		file.setPartsAttachFileWfId(partsAttachFileWfId);
		em.persist(file);
		em.flush();
		return em.find(MwtPartsAttachFileWf.class, partsAttachFileWfId);
	}

	/**
	 * ユーザデータを読み込んで、UserDataEntity形式で抽出
	 * @param tableName テーブル名
	 * @param sql SQL
	 * @param params SQL用パラメータ
	 * @return
	 */
	public List<UserDataEntity> selectUserData(String tableName, String sql, Object[] params) {
		// List<Map, Object>形式で抽出
		try {
			final Connection conn = em.unwrap(Connection.class);
			return NativeSqlUtils.select(conn, sql, params)
					.stream()
					.map(map -> new UserDataEntity(tableName, map))
					.collect(Collectors.toList());
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
