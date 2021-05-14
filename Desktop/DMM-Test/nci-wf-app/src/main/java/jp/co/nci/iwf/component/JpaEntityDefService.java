package jp.co.nci.iwf.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * JPAのアノテーションから、例えば、テーブル名やプライマリキーなどのエンティティの定義情報を取得するサービス。
 *
 */
@BizLogic
public class JpaEntityDefService extends BaseService {
	@Inject private CacheManager cm;

	/** キャッシュ */
	private CacheHolder<Class<?>, EntityDef> cache;

	/** エンティティの定義情報 */
	private class EntityDef {
		/** エンティティクラス */
		@SuppressWarnings("unused")
		public Class<? extends BaseJpaEntity> clazz;
		/** テーブル名 */
		public String tableName;
		/** プライマリキーのJavaフィールド名 */
		public String pkFieldName;
		/** プライマリキーのDBカラム名 */
		public String pkColumnName;
		/** ユニークキーのJavaフィールド名 */
		public List<String[]> ukFieldNames = new ArrayList<>();
		/** ユニークキーのDBカラム名 */
		public List<String[]> ukColumnNames = new ArrayList<>();
	}

	/**
	 * 初期化
	 */
	@PostConstruct
	public void init() {
		cache = cm.newInstance(CacheInterval.FOREVER);
	}

	/**
	 * 対象クラスのアノテーションから、エンティティのテーブル名やプライマリキーなどのエンティティ定義情報を取得＆キャッシュ。
	 * @param clazz
	 * @return
	 */
	private EntityDef parse(Class<? extends BaseJpaEntity> clazz) {
		EntityDef d = null;
		synchronized (cache) {
			d = cache.get(clazz);
			if (d == null) {
				d = new EntityDef();
				cache.put(clazz, d);

				d.clazz = clazz;
				final Table t = clazz.getAnnotation(Table.class);
				if (t != null) {
					// テーブル名
					d.tableName = t.name();
					// ユニークキー
					final UniqueConstraint[] constraints = t.uniqueConstraints();
					if (constraints != null) {
						for (UniqueConstraint uc : constraints) {
							// ユニークキーのカラム名
							d.ukColumnNames.add(uc.columnNames());
							// ユニークキーのJavaフィールド名
							List<String> fields = new ArrayList<>();
							for (String ucColName : uc.columnNames()) {
								for (Field f : clazz.getDeclaredFields()) {
									Column c = f.getDeclaredAnnotation(Column.class);
									String colName = (c != null ? c.name() : f.getName().toUpperCase());
									if (eq(ucColName, colName)) {
										fields.add(f.getName());
									}
								}
							}
							d.ukFieldNames.add(fields.toArray(new String[fields.size()]));
						}
					}
				}
				for (Field f : clazz.getDeclaredFields()) {
					final String fieldName = f.getName();
					final Column c = f.getAnnotation(Column.class);
					if (c == null && f.getAnnotation(Id.class) != null) {
						// @Columnが付与されてなく@Idはあるなら、PKだけは分かる
						d.pkColumnName = fieldName.toUpperCase();
						d.pkFieldName = fieldName;
					}
					else if (c != null) {
						String columnName = c.name();

						// プライマリキー
						if (f.getAnnotation(Id.class) != null) {
							d.pkColumnName = columnName;
							d.pkFieldName = fieldName;
						}
						// ユニークキー
						if (c.unique()) {
							d.ukColumnNames.add(new String[] { columnName });
							d.ukFieldNames.add(new String[] { fieldName });
						}
					}
				}
			}
		}
		return d;
	}


	/** エンティティのマッピング先テーブル名を返す */
	public <E extends BaseJpaEntity> String getTableName(Class<E> clazz) {
		EntityDef d = cache.get(clazz);
		if (d == null) {
			d = parse(clazz);
		}
		String tableName = d == null ? null : d.tableName;
		return tableName;
	}

	/** エンティティのプライマリキーのマッピング先カラム名を返す */
	public <E extends BaseJpaEntity> String getPkColumnName(Class<E> clazz) {
		EntityDef d = cache.get(clazz);
		if (d == null) {
			d = parse(clazz);
		}
		String pkColumnName = d == null ? null : d.pkColumnName;
		return pkColumnName;
	}

	/** エンティティのプライマリキーのフィールド名を返す */
	public <E extends BaseJpaEntity> String getPkFieldName(Class<E> clazz) {
		EntityDef d = cache.get(clazz);
		if (d == null) {
			d = parse(clazz);
		}
		String pkFieldName = d == null ? null : d.pkFieldName;
		return pkFieldName;
	}

	/** エンティティのユニークキーのJavaフィールド名を返す */
	public <E extends BaseJpaEntity> List<String[]> getUkFieldNames(Class<E> clazz) {
		EntityDef d = cache.get(clazz);
		if (d == null) {
			d = parse(clazz);
		}
		List<String[]> ukFieldNames = (d == null ? null : d.ukFieldNames);
		return ukFieldNames;
	}

	/** エンティティのユニークキーを返す */
	public <E extends BaseJpaEntity> List<String[]> getUkColumnNames(Class<E> clazz) {
		EntityDef d = cache.get(clazz);
		if (d == null) {
			d = parse(clazz);
		}
		List<String[]> ukColumnNames = (d == null ? null : d.ukColumnNames);
		return ukColumnNames;
	}
}
