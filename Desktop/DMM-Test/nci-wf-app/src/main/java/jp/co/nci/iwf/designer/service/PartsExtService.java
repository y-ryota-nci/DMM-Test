package jp.co.nci.iwf.designer.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツ拡張情報のサービス
 */
@ApplicationScoped
public class PartsExtService extends BaseService {
	/** パーツ定義のフィールド用キャッシュ */
	private static final Map<Class<? extends PartsDesign>, Map<String, Field>> cache = new ConcurrentHashMap<>(32);

	public String toJsonFromFields(PartsDesign design) {
		final Class<? extends PartsDesign> key = design.getClass();
		Map<String, Field> fields = cache.get(key);

		// キャッシュに無ければエントリ生成
		if (fields == null) {
			synchronized(cache) {
				// クラス名をキーにフィールド定義をキャッシュ
				fields = new ConcurrentHashMap<>(64);
				cache.put(key, fields);

				// フィールド定義Map生成
				final Set<String> names = new HashSet<>();
				names.addAll(asSet(design.extFieldNames()));
				names.add("defaultValue");	// 以前は特定パーツの固有フィールドだったが、共通フィールドに昇格した。このため、共通処理としてフィールドを拡張情報に保存する
				for (String name : names) {
					try {
						Field field = design.getClass().getField(name);
						fields.put(name, field);
					} catch (NoSuchFieldException e) {
						continue;
					} catch (SecurityException e) {
						throw new InternalServerErrorException(e);
					}
				};
			}
		}

		// フィールド名と値をMap化
		final Map<String, Object> map = new HashMap<>();
		for (Field f : fields.values()) {
			String name = f.getName();
			try {
				Object value = f.get(design);
				map.put(name, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InternalServerErrorException(e);
			}
		}
		// JSON文字列化
		return MiscUtils.toJsonFromObj(map);
	}

	/**
	 * 拡張情報の文字列から指定プロパティを抜き出して文字列として返す
	 * @param extInfo
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getExtValue(String extInfo, String propertyName) {
		if (isEmpty(extInfo))
			return null;

		final Map<String, Object> map = toObjFromJson(extInfo, Map.class);
		final Object val = map.get(propertyName);
		if (val == null)
			return null;
		return toStr(val);
	}
}
