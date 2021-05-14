package jp.co.nci.iwf.component.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.nci.iwf.component.CodeBook.CacheInterval;

/**
 * キャッシュ格納用クラス
 *
 * @param <K> 格納キー
 * @param <V> 格納する値
 */
public class CacheHolder<K, V> {
	/** キャッシュ */
	private Map<K, V> cache = new ConcurrentHashMap<>();
	/** タイムアウト時間(ミリ秒) */
	private long timeout;
	/** 最後にキャッシュへ値を追加したときの時間 */
	private long timestamp;
	/** タイムアウト時間を変更できるか */
	private CacheInterval interval;
	/** キャッシュ識別子 */
	private int id;

	/**
	 * 【同一パッケージ内のみ】コンストラクタ
	 * @param timeout タイムアウト（ミリ秒）
	 */
	CacheHolder(long timeout, CacheInterval interval, int id) {
		this.id = id;
		this.timeout = timeout;
		this.interval = interval;
	}

	/**
	 * 【同一パッケージ内のみ】新たなタイムアウト時間を設定する
	 * @param newTimeout タイムアウト(ミリ秒)
	 */
	void setTimeout(long newTimeout) {
		if (this.timeout != newTimeout) {
			// 新しいタイムアウトで期限切れしてないか？
			this.timeout = newTimeout;
			if (isExpired()) {
				clear();
			}
		}
	}

	/** タイムアウト時間を返す */
	public long getTimeout() {
		return this.timeout;
	}

	/**
	 * キャッシュをクリア
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * キャッシュへ追加
	 * @param key キー
	 * @param value 値
	 */
	public void put(K key, V value) {
		cache.put(key, value);
		timestamp = System.currentTimeMillis();
	}

	/**
	 * キャッシュへ一括追加
	 * @param map 追加するMap
	 */
	public void putAll(Map<K, V> map) {
		cache.putAll(map);
		timestamp = System.currentTimeMillis();
	}

	/**
	 * キャッシュから削除
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		if (isExpired()) {
			cache.clear();
			return null;
		}
		return cache.remove(key);
	}

	/**
	 * キャッシュからキーに該当する値を返す（タイムアウトしていたらNULLを返す）
	 * @param key
	 * @return
	 */
	public V get(K key) {
		if (isExpired()) {
			cache.clear();
			return null;
		}
		return cache.get(key);
	}

	/**
	 * キャッシュからキーに該当する値を文字列で返す（タイムアウトしていたらNULLを返す）
	 * @param key
	 * @return
	 */
	public String getString(K key) {
		V val = get(key);
		return (val == null ? null : val.toString());
	}

	/**
	 * キャッシュに該当キーがあるかを返す
	 * @param key キー
	 * @return
	 */
	public boolean containsKey(K key) {
		if (isExpired()) {
			cache.clear();
			return false;
		}
		return cache.containsKey(key);
	}

	/**
	 * 最後にキャッシュへ値を追加してからがタイムアウト時間を超過したか
	 * @return
	 */
	public boolean isExpired() {
		// タイムアウト時間が0以下ならタイムアウトしない
		if (timeout <= 0) {
			return false;
		}
		return (System.currentTimeMillis() - timestamp) > timeout;
	}

	/**
	 * キャッシュにエントリがあるか（タイムアウト時間を超過していてもエントリ無しとみなす）
	 * @return
	 */
	public boolean isEmpty() {
		if (isExpired()) {
			cache.clear();
			return true;
		}
		return cache.isEmpty();
	}

	/** タイムアウト時間を変更できるか */
	public boolean isChangeableTimeout() {
		return interval == CacheInterval.FROM_DATABASE;
	}

	/**
	 * リソース解放
	 */
	public void dispose() {
		clear();
	}

	/** キャッシュ識別子を返す */
	public int getId() {
		return id;
	}

	/** キーのSetを返す */
	public Set<K> keySet() {
		return cache.keySet();
	}

	/** キャッシュのエントリ数を返す */
	public int size() {
		return cache.size();
	}
}
