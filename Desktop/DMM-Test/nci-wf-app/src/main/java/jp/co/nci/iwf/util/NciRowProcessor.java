package jp.co.nci.iwf.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;

import com.ibm.icu.math.BigDecimal;

/**
 * Apache DBUtilsのBacisRowProcessorではCLOBが正しく扱えないので、
 * これを補正するためのRowProcessor実装。
 */
public class NciRowProcessor extends BasicRowProcessor {
	/** PropertyDescriptorのキャッシュ */
	private static Map<Class<?>, Map<String, PropertyDescriptor>> propCache = new HashMap<>(64);
	/** Fieldのキャッシュ */
	private static final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>(64);

	@Override
	public <T> T toBean(ResultSet rs, Class<? extends T> type) throws SQLException {
		final T bean = MiscUtils.newInstance(type);
		final Map<String, Field> fields = getFields(type);
		final Map<String, PropertyDescriptor> props = getPropertyDescriptors(type);

		final ResultSetMetaData rsmd = rs.getMetaData();
		final int count = rsmd.getColumnCount();
		for (int colNo = 1; colNo <= count; colNo++) {
			final String colName = rsmd.getColumnName(colNo);
			final String propName = MiscUtils.toCamelCase(colName);

			// セットすべきなのはプロパティ値か、フィールド値か
			if (props.containsKey(propName)) {
				PropertyDescriptor pd = props.get(propName);
				Object value = getValue(bean, pd.getPropertyType(), rs, colNo);
				MiscUtils.setPropertyValue(bean, pd, value);
			}
			else if (fields.containsKey(propName)) {
				Field field = fields.get(propName);
				Object value = getValue(bean, field.getDeclaringClass(), rs, colNo);
				MiscUtils.setFieldValue(bean, field, value);
			}
		}
		return bean;
	}

	/**
	 * 対象Beanの型に合わせて、ResultSetの呼び出すGETTERを切り替えて値を取得
	 * @param bean 対象Bean
	 * @param type 対象Beanのフィールド／プロパティの型
	 * @param rs ResultSet
	 * @param colNo ResultSetのカラムNo
	 * @return ResultSetの値
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private Object getValue(Object bean, Class<?> type, ResultSet rs, int colNo) throws SQLException {
		if (MiscUtils.in(type, String.class))
			return rs.getString(colNo);
		if (MiscUtils.in(type, java.sql.Timestamp.class, java.util.Date.class))
			return rs.getTimestamp(colNo);
		if (MiscUtils.in(type, java.sql.Date.class))
			return rs.getDate(colNo);
		if (MiscUtils.in(type, long.class, Long.class))
			return rs.getLong(colNo);
		if (MiscUtils.in(type, int.class, Integer.class))
			return rs.getInt(colNo);
		if (MiscUtils.in(type, double.class, Double.class))
			return rs.getDouble(colNo);
		if (MiscUtils.in(type, float.class, Float.class))
			return rs.getFloat(colNo);
		if (MiscUtils.in(type, short.class, Short.class))
			return rs.getShort(colNo);
		if (MiscUtils.in(type, boolean.class, Boolean.class))
			return rs.getBoolean(colNo);
		if (MiscUtils.in(type, BigDecimal.class))
			return rs.getBigDecimal(colNo);
		if (MiscUtils.in(type, byte.class, Byte.class))
			return rs.getBytes(colNo);

		// 不明な型はgetObject()で取得するが、こいつはJDBCドライバーの設定に依存する。変換が気に入らなければ、JDBCドライバーのドキュメントを見よ
		return rs.getObject(colNo);
	}

	/** 対象クラスのPropertyDescriptor定義Mapを返す */
	private Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> type) {
		Map<String, PropertyDescriptor> map = propCache.get(type);
		if (map == null) {
			synchronized (propCache) {
				map = propCache.get(type);
				if (map == null) {
					PropertyDescriptor[] props = MiscUtils.getPropertyDescriptors(type);
					map = new HashMap<>(props.length);
					for (PropertyDescriptor pd : props) {
						map.put(pd.getName(), pd);
					}
					propCache.put(type, map);
				}
			}
		}
		return map;
	}

	/** 対象クラスのフィールド定義Mapを返す */
	private Map<String, Field> getFields(Class<?> type) {
		Map<String, Field> map = fieldCache.get(type);
		if (map == null) {
			synchronized (fieldCache) {
				map = fieldCache.get(type);
				if (map == null) {
					Field[] fields = type.getFields();
					map = new HashMap<>(fields.length);
					for (Field f : fields) {
						map.put(f.getName(), f);
					}
					fieldCache.put(type, map);
				}
			}
		}
		return map;
	}

	@Override
	public <T> List<T> toBeanList(ResultSet rs, Class<? extends T> type) throws SQLException {
		List<T> list = new ArrayList<>();
		while (rs.next()) {
			T bean = toBean(rs, type);
			list.add(bean);
		}
		return list;
	}

	@Override
	public Object[] toArray(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		Object[] result = new Object[cols];

		for (int i = 0; i < cols; i++) {
			result[i] = rs.getObject(i + 1);

			// CLOBを文字列として読み込み
			if (result[i] instanceof Clob) {
				result[i] = rs.getString(i + 1);
			}
		}

		return result;
	}

	@Override
	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(i);
			}

			Object obj = rs.getObject(i);

			// CLOBを文字列として読み込み
			int type = rs.getMetaData().getColumnType(i);
			if (obj instanceof Clob) {
				obj = rs.getString(i);
			}
			else if (type == Types.TIMESTAMP) {
				obj = rs.getTimestamp(i);
			}
			result.put(columnName, obj);
		}

		return result;
	}


	/**
	 * A Map that converts all keys to lowercase Strings for case insensitive
	 * lookups.  This is needed for the toMap() implementation because
	 * databases don't consistently handle the casing of column names.
	 *
	 * <p>The keys are stored as they are given [BUG #DBUTILS-34], so we maintain
	 * an internal mapping from lowercase keys to the real keys in order to
	 * achieve the case insensitive lookup.
	 *
	 * <p>Note: This implementation does not allow {@code null}
	 * for key, whereas {@link LinkedHashMap} does, because of the code:
	 * <pre>
	 * key.toString().toLowerCase()
	 * </pre>
	 */
	private static class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {
		/**
		 * The internal mapping from lowercase keys to the real keys.
		 *
		 * <p>
		 * Any query operation using the key
		 * ({@link #get(Object)}, {@link #containsKey(Object)})
		 * is done in three steps:
		 * <ul>
		 * <li>convert the parameter key to lower case</li>
		 * <li>get the actual key that corresponds to the lower case key</li>
		 * <li>query the map with the actual key</li>
		 * </ul>
		 * </p>
		 */
		private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

		/**
		 * Required for serialization support.
		 *
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = -2848100435296897392L;

		/** {@inheritDoc} */
		@Override
		public boolean containsKey(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.containsKey(realKey);
			// Possible optimisation here:
			// Since the lowerCaseMap contains a mapping for all the keys,
			// we could just do this:
			// return lowerCaseMap.containsKey(key.toString().toLowerCase());
		}

		/** {@inheritDoc} */
		@Override
		public Object get(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.get(realKey);
		}

		/** {@inheritDoc} */
		@Override
		public Object put(String key, Object value) {
			/*
			 * In order to keep the map and lowerCaseMap synchronized, we have
			 * to remove the old mapping before putting the new one. Indeed,
			 * oldKey and key are not necessaliry equals. (That's why we call
			 * super.remove(oldKey) and not just super.put(key, value))
			 */
			Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
			Object oldValue = super.remove(oldKey);
			super.put(key, value);
			return oldValue;
		}

		/** {@inheritDoc} */
		@Override
		public void putAll(Map<? extends String, ?> m) {
			for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				this.put(key, value);
			}
		}

		/** {@inheritDoc} */
		@Override
		public Object remove(Object key) {
			Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
			return super.remove(realKey);
		}
	}
}
