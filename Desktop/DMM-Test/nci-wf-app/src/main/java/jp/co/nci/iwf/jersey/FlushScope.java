package jp.co.nci.iwf.jersey;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * FlushスコープのMap。
 * get()すると、値は削除される
 */
public class FlushScope {
	private ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

	/**
	 * キーで値を保存。
	 * @param key キー
	 * @param value 値
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E put(String key, Object value) {
		return (E)map.put(key, value);
	}

	/**
	 * キーで値を保存。（指定されたキーがまだ値と関連付けられていない場合は、指定された値に関連付けます）
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E putIfAbsent(String key, Object value) {
		return (E)map.putIfAbsent(key, value);
	}

	/**
	 * 値を返し、かつFlushScopeから保存内容を破棄
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(String key) {
		return (E)map.remove(key);
	}

	/**
	 * FlushScopeの保存内容をすべて破棄
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * FlushScopeの保存内容のキーを列挙
	 * @return
	 */
	public Set<String> keySet() {
		return map.keySet();
	}
}
