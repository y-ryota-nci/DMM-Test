package jp.co.nci.iwf.jersey.base;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;

import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultSetType;
import org.eclipse.persistence.queries.Cursor;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.input.RowHandler;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * DBアクセスの基底クラス
 */
public abstract class BaseRepository extends MiscUtils implements CodeBook {
	/** JPAの永続化管理Context */
	@Inject
	protected EntityManager em;
	@Inject
	private SqlService sqlService;

	protected static final Object[] NO_PARAM = new Object[0];

	@PostConstruct
	public void init() {
	}

	/**
	 * SQLをYAMLファイルから取得
	 * @param id
	 * @return
	 */
	protected String getSql(String id) {
		if (isEmpty(id))
			throw new NullPointerException("SQLを取得しようとしましたが、IDが空でした");

		String sql = sqlService.get(id);
		if (isEmpty(sql))
			throw new NullPointerException(
					String.format("YAMLファイルに ID=%s をもつSQL定義がありません", id));
		return sql;
	}

	/**
	 * " order by [alias][sortColumn]"の形式で文字列化
	 * @param sortColumn
	 * @param alias
	 * @return
	 */
	protected String toOrderBy(String sortColumn, String alias) {
		if (isEmpty(sortColumn)) return "";
		StringBuilder sb = new StringBuilder(" order by ");
		if (isEmpty(alias)) {
			sb.append(sortColumn);
		}
		else {
			String[] cols = sortColumn.split(",\\s*");
			for (int i = 0; i < cols.length; i++) {
				if (i > 0) sb.append(", ");
				sb.append(alias);
				sb.append(cols[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * JPA経由でQueryへパラメータをセット
	 * @param q Query
	 * @param params パラメータList
	 */
	protected void putParams(Query q, Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				final Object value = params[i];
				q.setParameter(i + 1, value);
			}
		}
	}

	/**
	 * JPA経由でSQL実行
	 * @param sql SQL（パラメータ名は '?'で埋め込むこと）
	 * @param params パラメータ
	 * @return
	 */
	protected int execSql(CharSequence sql) {
		return execSql(sql, NO_PARAM);
	}

	/**
	 * JPA経由でSQL実行
	 * @param sql SQL（パラメータ名は '?'で埋め込むこと）
	 * @param params パラメータ
	 * @return
	 */
	protected int execSql(CharSequence sql, Object[] params) {
		final Query q = em.createNativeQuery(sql.toString());
		putParams(q, params);
		return q.executeUpdate();
	}

	/**
	 * JPA経由でSQL実行
	 * @param resultClass 抽出結果エンティティの型。@Entityでアノテーションされていること。
	 * @param sql SQL（パラメータ名は '?'で埋め込むこと）
	 * @return
	 */
	protected <E extends BaseJpaEntity> List<E> select(Class<E> resultClass, CharSequence sql) {
		return select(resultClass, sql, NO_PARAM);
	}

	/**
	 * JPA経由でSQL実行
	 * @param resultClass 抽出結果エンティティの型。@Entityでアノテーションされていること。
	 * @param sql SQL（パラメータ名は '?'で埋め込むこと）
	 * @param params パラメータ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <E extends BaseJpaEntity> List<E> select(Class<E> resultClass, CharSequence sql, Object[] params) {
		throwIfNoEntityAnnotation(resultClass);
		final Query q = em.createNativeQuery(sql.toString(), resultClass);
		putParams(q, params);
		return (List<E>)q.getResultList();
	}

	/**
	 * SQL実行
	 * @param resultClass 抽出結果エンティティの型。@Entityでアノテーションされていること。
	 * @param sql SQL（パラメータ名は ':name'で埋め込むこと）
	 * @param params パラメータMap
	 * @param pageNo ページ番号
	 * @param pageSize ページあたりの行数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <E> List<E> select(Class<E> resultClass, CharSequence sql, Object[] params, int pageNo, int pageSize) {
		throwIfNoEntityAnnotation(resultClass);
		final int startPosition = toStartPosition(pageNo, pageSize);
		final Query q = em.createNativeQuery(sql.toString(), resultClass);
		putParams(q, params);
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		return (List<E>)q.getResultList();
	}

	/**
	 * JPA経由でSQL実行
	 * @param resultClass 抽出結果エンティティの型。@Entityでアノテーションされていること。
	 * @param sql SQL（パラメータ名は '?'で埋め込むこと）
	 * @param params パラメータ
	 * @return
	 */
	protected <E extends BaseJpaEntity> E selectOne(Class<E> resultClass, CharSequence sql, Object[] params) {
		return select(resultClass, sql, params).stream().findFirst().orElse(null);
	}

	/**
	 * ページ番号とページあたりの行数から、対象ページの開始位置を計算して返す
	 * @param pageNo ページ番号
	 * @param pageSize ページあたりの行数
	 * @return 対象ページの開始位置
	 */
	protected int toStartPosition(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;	// JPAの Query.setFirstResultは0オリジン
	}

	/**
	 * JPA経由でカウントSQL実行
	 * @param sql SQL
	 * @return
	 */
	protected long count(CharSequence sql) {
		return count(sql, NO_PARAM);
	}

	/**
	 * JPA経由でカウントSQL実行
	 * @param sql SQL（パラメータがList／配列ならパラメータ名は '?'、Mapなら ':name'で埋め込むこと）
	 * @param params パラメータ。型は下記のいずれか。List<?>／Object[]／Map<String, ?>
	 * @return
	 */
	protected int count(CharSequence sql, Object[] params) {
		final Query q = em.createNativeQuery(sql.toString());
		putParams(q, params);
		Number count = (Number)q.getSingleResult();
		return count == null ? 0 : count.intValue();
	}

	/**
	 * 対象クラスが {code @Entity}でアノテーションされていないと例外スロー
	 * @param resultClass
	 */
	protected void throwIfNoEntityAnnotation(Class<?> resultClass) {
		if (resultClass.getAnnotation(Entity.class) == null) {
			throw new IllegalArgumentException(String.format("Queryの検索結果クラス '%s' には @Entity でのアノテーションがありません。", resultClass.getSimpleName()));
		}
	}

	/**
	 * JPA経由でSQLを実行し、その検索結果を行単位で処理を行う【大量データ処理用】。
	 * RowHandlerが処理した行は即時破棄されるため、メモリ効率が良い。
	 * （実装では前方スクロールなカーソルを使用している）
	 * @param resultClass 検索結果格納クラス
	 * @param sql SQL
	 * @param rowHandler 検索結果を行単位で処理するハンドラー
	 * @param params 検索条件パラメータ
	 * @throws IOException
	 */
	protected <T> void select(Class<T> resultClass, CharSequence sql, RowHandler<T> rowHandler) {
		select(resultClass, sql, NO_PARAM, rowHandler);
	}

	/**
	 * JPA経由でSQLを実行し、その検索結果を行単位で処理を行う【大量データ処理用】。
	 * RowHandlerが処理した行は即時破棄されるため、メモリ効率が良い。
	 * （実装では前方スクロールなカーソルを使用している）
	 * @param resultClass 検索結果格納クラス
	 * @param sql SQL
	 * @param params 検索条件パラメータ
	 * @param rowHandler 検索結果を行単位で処理するハンドラー
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected <T> void select(Class<T> resultClass, CharSequence sql, Object[] params, RowHandler<T> rowHandler) {
		final Query query = em.createNativeQuery(sql.toString(), resultClass);
		putParams(query, params);

		// 前方スクロール指定
		final int fetchSize = 100;
		query.setHint(QueryHints.RESULT_SET_TYPE, ResultSetType.ForwardOnly);
		query.setHint(QueryHints.SCROLLABLE_CURSOR, true);
		query.setHint(QueryHints.JDBC_FETCH_SIZE, fetchSize);
		query.setHint(QueryHints.JDBC_TIMEOUT, TimeUnit.MINUTES.toSeconds(30));
		query.setHint(QueryHints.READ_ONLY, true);

		Cursor cursor = null;
		try {
			cursor = (Cursor)query.getSingleResult();
			while (cursor.hasNext()) {
				T entity = (T)cursor.next();
				if (!rowHandler.accept(entity)) {
					break;
				}
			}
			cursor.close();
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e);
		}
		finally {
			if (cursor != null && !cursor.isClosed())
				try { cursor.close(); }
				catch (Exception e) {
					LoggerFactory.getLogger(getClass()).warn(e.getMessage(), e);
				}
		}
	}

	/**
	 * IN句に相当するSQLを生成。「column in (?, ?, .., ?)」
	 * @param column カラム名
	 * @param count 出力する「?」の数
	 * @return
	 */
	protected String toInListSql(String column, int count) {
		StringBuilder sb = new StringBuilder(32);
		sb.append(" ").append(column).append(" in (");
		for (int i = 0; i < count; i++) {
			sb.append(i == 0 ? "?" : ", ?");
		}
		sb.append(") ");
		return sb.toString();
	}

	/** ORDER BY句を生成 */
	protected StringBuilder toSortSql(String sortColumn, boolean sortAsc) {
		final StringBuilder orderBy = new StringBuilder(64);
		orderBy.append(" order by ");
		String[] cols = sortColumn.split(",\\s*");
		for (int i = 0; i < cols.length; i++){
			orderBy.append(i == 0 ? "" : ", ");
			orderBy.append(cols[i]).append(sortAsc ? " ASC" : " DESC");
		}
		return orderBy;
	}

	/**
	 * コネクション取得。
	 * このConnectionはJPAが管理しているので、勝手にクローズしてはダメ。
	 * また、@Transactional宣言されていないとConnectionが取得できない。
	 * ようは EntityManagerのトランザクション制御下にあるConnectionを使いまわしているのだ。
	 * @return
	 */
	protected Connection getConnection() {
		final Connection conn = em.unwrap(Connection.class);
		if (conn == null)
			throw new InternalServerErrorException(
					"Connectionの取得に失敗しました。"
					+ "スコープ内にアノテーション「@Transactional」宣言があるかを確認ください。"
					+ "当実装は JPAに依存しているため、当関数を呼び出す前に "
					+ "@Transactional宣言が必須です。");
		return conn;
	}
}
