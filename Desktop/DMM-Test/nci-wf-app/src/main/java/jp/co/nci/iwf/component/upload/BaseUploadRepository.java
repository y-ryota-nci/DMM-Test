package jp.co.nci.iwf.component.upload;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.Table;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.JpaEntityDefService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;

/**
 * 定義アップロード時のID置換
 */
public abstract class BaseUploadRepository extends BaseRepository {
	@Inject protected JpaEntityDefService jpaEntityDef;
	@Inject protected NumberingService numbering;

	protected static final String TABLE_NAME = quotePattern("${TABLE_NAME}");
	protected static final String FILTER = quotePattern("${FILTER}");
	protected static final String SET_FIELD = quotePattern("${SET_FIELD}");


	/**
	 * 多言語対応マスタ以外のテーブルの差分更新
	 * @param <E> 「アップロードされたエンティティ」の型
	 * @param <P> 「アップロードされたエンティティ」の「親エンティティ」の型
	 * @param uploadList アップロードされたエンティティのリスト
	 * @param currentList 現在のDBのエンティティ
	 * @param changedPKsMap PK変更点Map
	 * @param deleteIfNotUse アップロードファイルにない既存レコードを削除するならtrue.
	 * @param parentIds 親テーブルのIDリスト
	 */
	protected <E extends MwmBaseJpaEntity, P extends MwmBaseJpaEntity> void upsert(
			List<E> uploadList,
			List<E> currentList,
			ChangedPKsMap changedPKsMap,
			boolean deleteIfNotUse
	) {
		upsert(uploadList, currentList, changedPKsMap, deleteIfNotUse, null);
	}

	/**
	 * 多言語対応マスタ以外のテーブルの差分更新
	 * @param <E> 「アップロードされたエンティティ」の型
	 * @param <P> 「アップロードされたエンティティ」の「親エンティティ」の型
	 * @param uploadList アップロードされたエンティティのリスト
	 * @param currentList 現在のDBのエンティティ
	 * @param changedPKsMap PK変更点Map
	 * @param deleteIfNotUse アップロードファイルにない既存レコードを削除するならtrue
	 * @param parentList 親テーブルのリスト。指定していれば当エンティティに親エンティティがあるときだけ、自身を削除
	 */
	protected <E extends MwmBaseJpaEntity, P extends MwmBaseJpaEntity> void upsert(
			List<E> uploadList,
			List<E> currentList,
			ChangedPKsMap changedPKsMap,
			boolean deleteIfNotUse,
			List<P> parentList
	) {
		// 対象エンティティのクラスを求める
		// 現在のDBのエンティティを、PKをキーにMap化
		Map<Long, E> currents = currentList.stream().collect(Collectors.toMap(c -> getPkValue(c), c -> c));

		if (!uploadList.isEmpty()) {
			for (E upload : uploadList) {
				// アップロードされたレコードは削除対象から外す
				final Long id = getPkValue(upload);
				currents.remove(id);

				// 差分更新
				if (upload.getVersion() == null)
					em.persist(upload);
				else
					em.merge(upload);
			}
		}
		// 残余は不要レコードなので削除
		if (deleteIfNotUse) {
			Set<Long> parentIds = null;
			String parentIdName = null;
			if (parentList != null && !parentList.isEmpty()) {
				parentIds = parentList.stream().map(p -> getPkValue(p)).collect(Collectors.toSet());
				parentIdName = jpaEntityDef.getPkFieldName(parentList.get(0).getClass());
			}

			for (E current : currents.values()) {
				// 自分エンティティの親が使われているなら、親から子要素が減らされたものとみなして、削除してよい。
				// 自エンティティの親自体がなくなったなら、子要素側を削除してよいかは判定できないので削除してはダメ。
				// 例えば、画面からコンテナを参照しなくなったら、以前コンテナ内で参照していたパーツもアップロードファイルには含まれなくなるが、
				// それはこの画面でそのパーツを使わなくなっただけなので、データベースから削除してはダメ（他のコンテナでは使っているかもしれない）
				// 画面でコンテナを参照しているのにその配下であるパーツがアップロードファイルに含まれなければ、
				// それはコンテナからパーツが削除されたということなので削除する。
				if (parentList != null && parentList.isEmpty())
					continue;	// 親が参照されなくなったので自分が削除されるべきか判断できなくなった -> 削除しない
				else if (parentIds != null && isNotEmpty(parentIdName)) {
					final Long parentId = getPropertyValue(current, parentIdName);
					assert parentId != null : "親IDがNULLなんておかしくないか？" + current.getClass().getSimpleName();
					if (!parentIds.contains(parentId))	// 親が参照されなくなったので自分が削除されるべきか判断できなくなった -> 削除しない
						continue;
				}
				em.remove(current);
			}
		}

		em.flush();
	}

	/** エンティティのPK値を返す */
	protected Long getPkValue(MwmBaseJpaEntity e) {
		String pkField = jpaEntityDef.getPkFieldName(e.getClass());
		Long id = getPropertyValue(e, pkField);
		return id;
	}

	/** 多言語対応マスタのキー文字列生成 */
	protected String toKey(MwmMultilingual m) {
		return m.getTableName() + "/" + m.getId() + "/" + m.getColumnName() + "@" + m.getLocaleCode();
	}


	/** 多言語対応マスタの差分更新 */
	protected void upsertMwmMultilingual(List<MwmMultilingual> uploads, Map<String, MwmMultilingual> currents, ChangedPKsMap changedPKsMap) {
		for (MwmMultilingual upload : uploads) {
			// 差分更新
			if (upload.getVersion() == null)
				em.persist(upload);
			else
				em.merge(upload);

			// アップロードされたレコードは削除対象から外す
			final String key = toKey(upload);
			currents.remove(key);
		}
		// 残余は不要レコードなので削除
		for (MwmMultilingual current : currents.values())
			em.remove(current);

		em.flush();
	}

	/**
	 * Delete＆Insertを行う
	 * @param uploads 新レコード（インサートするレコード）
	 * @param currents 既存レコード（＝削除するレコード）
	 */
	protected <E extends MwmBaseJpaEntity> void deleteAndInsert(List<E> uploads, List<E> currents) {
		// 既存をPKベースで一括削除
		for (E current : currents) {
			em.remove(current);
		}
		em.flush();

		// ユニークキーベースで一括削除
		for (E upload : uploads) {
			final E current = getByUniqueKey(upload);
			if (current != null)
				em.remove(current);
		}
		em.flush();

		// 一括インサート
		if (uploads.size() > 0) {
			// リフレクションでPKをセットするため、PKのsetterメソッドを求める
			final E sample = uploads.get(0);
			final Class<? extends MwmBaseJpaEntity> clazz = sample.getClass();
			final String pkFieldName = jpaEntityDef.getPkFieldName(clazz);
			final Method setter = getPropertyDescriptor(sample, pkFieldName).getWriteMethod();

			// PKを採番。大量インサート用に事前に必要行数分を採番しておき、逐次インクリメントして使う
			long id = numbering.newPK(clazz, uploads.size());

			// インサート
			for (E upload : uploads) {
				try {
					setter.invoke(upload, id++);	// PKのセット
					upload.setVersion(null);
					em.persist(upload);
				}
				catch (SecurityException | IllegalArgumentException
						| IllegalAccessException | InvocationTargetException e) {
					throw new InternalServerErrorException(e);
				}
			}
		}
		em.flush();
	}
	/** ユニークキーをもとに対象データを抽出 */
	@SuppressWarnings("unchecked")
	public <E extends MwmBaseJpaEntity> E getByUniqueKey(E entity) {
		final Class<E> clazz = (Class<E>)entity.getClass();
		final List<String[]> ukColumnNames = jpaEntityDef.getUkColumnNames(clazz);
		final List<String[]> ukFieldNames = jpaEntityDef.getUkFieldNames(clazz);
		final List<Object> params = new ArrayList<>();
		final List<String> uniqueClauses = new ArrayList<>();

		if (ukColumnNames.isEmpty())
			throw new IllegalArgumentException("JPAエンティティのアノテーションで、ユニークキーが未定義です。Entity=" + clazz.getSimpleName());

		for (int i = 0; i < ukColumnNames.size(); i++) {
			final List<String> clause = new ArrayList<>();
			final String[] fields = ukFieldNames.get(i);
			final String[] colNames = ukColumnNames.get(i);
			for (int j = 0; j < fields.length; j++) {
				String field = fields[j];
				String colName = colNames[j];
				Object value = getPropertyValue(entity, field);
				if (isEmpty(value)) {
					clause.add(colName + " is null");
				} else {
					clause.add(colName + " = ?");
					if (value instanceof Enum)
						params.add(value.toString());
					else
						params.add(value);
				}
			}
			uniqueClauses.add(String.join(" and ", clause));
		}
		// uniqueClauses１つで１ユニークキーを示す。したがって uniqueClausesをORで結合しても必ずユニークになる
		String filter = "(" + String.join(") or (", uniqueClauses) + ")";

		final String sql = getSql("UP0001_11")
				.replaceFirst(TABLE_NAME, jpaEntityDef.getTableName(clazz))
				.replaceFirst(FILTER, filter);

		final List<E> list = select(clazz, sql.toString(), params.toArray());
		if (list.isEmpty())
			return null;
		if (list.size() > 1 && ukColumnNames != null && ukColumnNames.size() > 0)
			throw new InternalServerErrorException("ユニークキーで抽出した結果が2件以上でした key=" + clazz.getSimpleName() + ", 件数=" + list.size());
		return list.get(0);
	}

	/** 親テーブルのキーをもとに対象データを抽出 */
	public <E extends MwmBaseJpaEntity> List<E> getByParentId(Class<E> clazz, String columnName, Object key) {
		final List<Object> params = new ArrayList<>();
		final String filter;
		if (key instanceof Collection) {
			Collection<?> ids = (Collection<?>)key;
			filter = toInListSql(columnName, ids.size());
			params.addAll(ids);
		} else {
			filter = columnName + " = ?";
			params.add(key);
		}
		final String tableName = clazz.getAnnotation(Table.class).name();
		final String sql = getSql("UP0001_12")
				.replaceFirst(TABLE_NAME, tableName)
				.replaceFirst(FILTER, filter);
		return select(clazz, sql, params.toArray());
	}

}
