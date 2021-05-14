package jp.co.nci.iwf.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * BeanからMapへ変換するためのコンバーター
 */
public class BeanToMapConveter {
	protected static final PropertyUtilsBean propUtil = new PropertyUtilsBean();

	/**
	 * 対象の属性名と属性値でMap化
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> toMap(Object bean) {
		final Map<String, Object> map;
		if (bean == null) {
			map = new HashMap<>();
		}
		else if (bean instanceof Map) {
			// Mapか？
			map = fromMap( (Map<?, ?>)bean );
		}
		else if (MiscUtils.isUseField(bean)) {
			// Beanの属性はフィールド直読み
			map = fromFields(bean);
		}
		else {
			// Beanの属性はgetter経由
			map = fromGetter(bean);
		}
		return map;
	}

	/**
	 * Beanの属性をgetter経由で取得してMap化
	 * @param bean
	 * @return
	 */
	private static Map<String, Object> fromGetter(Object bean) {
		final PropertyDescriptor[] descriptors = propUtil.getPropertyDescriptors(bean);
		final Map<String, Object> map = new HashMap<>(descriptors.length);
		for (PropertyDescriptor d : descriptors) {
			String name = d.getName();
			if (propUtil.isReadable(bean, name)) {
				try {
					final Object value = propUtil.getSimpleProperty(bean, name);
					map.put(name, value);
				}
				catch (NoSuchMethodException e) {
					; // Should not happen
				}
				catch (Exception e) {
					throw new InternalServerErrorException(e);
				}
			}
		}
		return map;
	}

	/**
	 * Beanの属性をフィールド経由で取得してMap化
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> fromFields(Object bean) {
		final Field[] fields = bean.getClass().getFields();
		final Map<String, Object> map = new HashMap<>(fields.length);
		for (Field f : fields) {
			try {
				map.put(f.getName(), f.get(bean));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
		}
		return map;
	}

	/**
	 * BeanをMapとみなして、そのキー／値でMap化
	 * @param bean
	 * @return
	 */
	private static Map<String, Object> fromMap(Map<?, ?> bean) {
		final Map<String, Object> map = new HashMap<>(bean.size());
		for (Object key : bean.keySet()) {
			map.put(key.toString(), bean.get(key));
		}
		return map;
	}
}
