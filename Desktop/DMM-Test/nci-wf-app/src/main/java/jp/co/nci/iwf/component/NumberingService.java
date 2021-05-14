package jp.co.nci.iwf.component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * 採番サービス
 */
@BizLogic
public class NumberingService extends MiscUtils {
	/** JPAエンティティ定義サービス */
	@Inject private JpaEntityDefService jpaEntityDef;
	/** SQLサービス */
	@Inject private SqlService sqlService;

	/**
	 * 採番
	 * @param tableName テーブル名
	 * @param columnName カラム名
	 * @return
	 */
	@Transactional
	public long next(String tableName, String columnName) {
		return next(tableName, columnName, 1L);
	}

	/**
	 * 採番
	 * @param tableName テーブル名
	 * @param prefixColName カラム名のプレフィックス
	 * @param suffixeColNames カラム名のサフィックス(プレフィックスと'_'で文字連結してカラム名とする)
	 * @return
	 */
	@Transactional
	public long next(String tableName, String prefixColName, String...suffixeColNames) {
		StringBuilder colName = new StringBuilder(16 * (suffixeColNames.length + 1));
		colName.append(prefixColName);
		for (String option : suffixeColNames) {
			colName.append("_").append(option);
		}
		return next(tableName, colName.toString(), 1L);
	}

	/**
	 * 一括採番.
	 * @param tableName テーブル名
	 * @param columnName カラム名
	 * @param count 現在値から進めるカウント数。
	 * 				通常は1つずつ採番するが、複数個を採番することが事前に分かっているなら、
	 * 				パフォーマンスを向上させるためにその分を一度に採番させたい場合に2以上を指定する。
	 * @return
	 */
	private long next(String tableName, String columnName, long count) {
		if (count < 1)
			throw new IllegalArgumentException("countには1未満を指定できません");

		final Object[] params = { tableName, columnName, count };
		final String sql = getSql("CM0007");
		NativeSqlUtils.debugSql(sql, params);

		// 独立したコネクションを取得してSQL発行＆コミット
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			// 元のautoCommitを退避
			boolean original = conn.getAutoCommit();
			conn.setAutoCommit(false);

			try (CallableStatement cs = conn.prepareCall(sql)) {
				int i = 0;
				for (Object p : params) {
					cs.setObject(++i, p);
				}
				try (ResultSet rs = cs.executeQuery()) {
					if (rs.next()) {
						return rs.getLong(1);
					}
				}
				throw new InternalServerErrorException("採番できませんでした");
			}
			finally {
				// 元のautoCommitを復元
				conn.commit();
				conn.setAutoCommit(original);
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * クラスに付与されたアノテーション（ @Table, @Id, @Column）からテーブル名とPKカラム名を求めて、プライマリキーを採番する
	 * @param clazz JPAエンティティクラス
	 * @return
	 */
	public <E extends BaseJpaEntity> long newPK(Class<E> clazz) {
		return newPK(clazz, 1);
	}

	/**
	 * クラスに付与されたアノテーション（ @Table, @Id, @Column）からテーブル名とPKカラム名を求めて、プライマリキーを採番する
	 * @param clazz JPAエンティティクラス
	 * @return
	 */
	public <E extends BaseJpaEntity> long newPK(Class<E> clazz, long count) {
		// @Tableが付与されたクラス
		String tableName = jpaEntityDef.getTableName(clazz);
		if (isEmpty(tableName))
			throw new InternalServerErrorException("Class[" + clazz.getName() + "]には@Tableのアノテーションが付与されていないため、テーブル名を求めることが出来ません");

		// @Id と @Column が付与されたフィールド
		String columnName = jpaEntityDef.getPkColumnName(clazz);
		if (isEmpty(columnName))
			throw new InternalServerErrorException("Class[" + clazz.getName() + "]には @Id, @Columnのアノテーションがフィールドに付与されていないため、プライマリーキーを求めることが出来ません");

		return next(tableName, columnName, count);
	}

	/**
	 * クラスに付与されたアノテーション（ @Table, @Id, @Column）からテーブル名とPKカラム名を求めて、採番マスタの現在値を書き換える
	 * （ただし、採番マスタ.現在値＞新しい値なら更新されない）
	 * @param clazz クラス
	 * @param newValue 新しい値
	 */
	public <E extends BaseJpaEntity>  void sync(Class<E> clazz, long newValue) {
		// @Tableが付与されたクラス
		String tableName = jpaEntityDef.getTableName(clazz);
		if (isEmpty(tableName))
			throw new InternalServerErrorException("Class[" + clazz.getName() + "]には@Tableのアノテーションが付与されていないため、テーブル名を求めることが出来ません");

		// @Id と @Column が付与されたフィールド
		String columnName = jpaEntityDef.getPkColumnName(clazz);
		if (isEmpty(columnName))
			throw new InternalServerErrorException("Class[" + clazz.getName() + "]には @Id, @Columnのアノテーションがフィールドに付与されていないため、プライマリーキーを求めることが出来ません");

		sync(tableName, columnName, newValue);
	}

	/**
	 * 採番マスタの現在値を書き換える
	 * （ただし、採番マスタ.現在値＞新しい値なら更新されない）
	 * @param tableName テーブル名
	 * @param columnName カラム名
	 * @param newValue 新しい値
	 * @return 採番マスタの現在値
	 */
	@Transactional
	public Long sync(String tableName, String columnName, long newValue) {
		final Object[] params = { tableName, columnName, newValue };
		final String sql = getSql("CM0017");
		NativeSqlUtils.debugSql(sql, params);

		// 独立したコネクションを取得してSQL発行＆コミット
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			// 元のautoCommitを退避
			boolean original = conn.getAutoCommit();
			conn.setAutoCommit(true);

			try (CallableStatement cs = conn.prepareCall(sql)) {
				int i = 0;
				for (Object p : params)
					cs.setObject(++i, p);
				try (ResultSet rs = cs.executeQuery()) {
					if (rs.next())
						return rs.getLong(1);
		}
				throw new InternalServerErrorException("採番マスタの現在値の書き換えが出来ませんでした");
			}
			finally {
				// 元のautoCommitを復元
				conn.setAutoCommit(original);
			}
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}


	/**
	 * SQLをYAMLファイルから取得
	 * @param id
	 * @return
	 */
	protected String getSql(String id) {
		String sql = sqlService.get(id);
		if (isEmpty(sql))
			throw new NullPointerException(
					String.format("YAMLファイルに ID=%s をもつSQL定義がありません", id));
		return sql;
	}
}
