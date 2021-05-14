package jp.co.nci.iwf.component.system;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * YAMLファイルから取得した値をMapとして保持し、getXXXを呼び出した際に適切な型変換を行うMap
 */
public class YamlMap extends ConcurrentHashMap<String, Object> {

	public YamlMap() {
	}

	public YamlMap(int size) {
		super(size);
	}

	public YamlMap(Map<String, Object> src) {
		super(src);
	}

	/** キーに対応する値をStringで返す */
	public String getString(String key) {
		return MiscUtils.toStr(get(key));
	}

	/** キーに対応する値をLongで返す */
	public Long getLong(String key) {
		Object v = get(key);
		if (v == null)
			return null;
		if (v instanceof Number)
			return ((Number)v).longValue();
		return Long.valueOf(v.toString());
	}

	/** キーに対応する値をIntegerで返す */
	public Integer getInt(String key) {
		Object v = get(key);
		if (v == null)
			return null;
		if (v instanceof Number)
			return ((Number)v).intValue();
		return Integer.valueOf(v.toString());
	}

	/** キーに対応する値をBigDecimalで返す */
	public BigDecimal getBigDecimal(String key) {
		Object v = get(key);
		if (v == null)
			return null;
		if (v instanceof Number)
			return new BigDecimal(((Number)v).doubleValue());
		return new BigDecimal(v.toString());
	}

	/** キーに対応する値をDoubleで返す */
	public Double getDouble(String key) {
		Object v = get(key);
		if (v == null)
			return null;
		if (v instanceof Number)
			return ((Number)v).doubleValue();
		return MiscUtils.toDouble(v.toString());
	}

	public Boolean getBoolean(String key) {
		final Object v = get(key);
		if (v == null) return null;
		if (v instanceof String) return MiscUtils.toBool((String)v);
		if (v instanceof Boolean) return (Boolean)v;
		if (v instanceof Number) return ((Number)v).intValue() != 0;
		return MiscUtils.toBool(v.toString());
	}
}
