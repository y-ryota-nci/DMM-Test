package jp.co.nci.iwf.component.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.CodeBook.CacheInterval;

/**
 * 言語別キャッシュホルダー（言語コードをキー指定できる）
 *
 * @param <K> キャッシュキー
 * @param <V> キャッシュ値
 */
public class LocaledCacheHolder<K, V> {
	/** 言語別のキャッシュホルダMap */
	private Map<String, CacheHolder<K, V>> cache = new ConcurrentHashMap<>();

	/** キャッシュ間隔 */
	private CacheInterval cacheInterval;

	/**
	 * コンストラクタ
	 * @param interval
	 */
	LocaledCacheHolder(CacheInterval interval) {
		this.cacheInterval = interval;
	}

	/**
	 * 言語コード指定でキャッシュ
	 * @param localeCode 言語コード
	 * @param key キャッシュキー
	 * @param value キャッシュ値
	 */
	public void put(String localeCode, K key, V value) {
		final CacheHolder<K, V> holder = getHolder(localeCode);
		holder.put(key, value);
	}

	/**
	 * その言語コードでキーの対応値を返す
	 * @param localeCode 言語コード
	 * @param key キャッシュキー
	 * @return キャッシュ値
	 */
	public V get(String localeCode, K key) {
		final CacheHolder<K, V> holder = getHolder(localeCode);
		return holder.get(key);
	}

	/**
	 * その言語コードでキーが存在するか
	 * @param localeCode 言語コード
	 * @param key キャッシュキー
	 * @return
	 */
	public boolean containsKey(String localeCode, K key) {
		final CacheHolder<K, V> holder = getHolder(localeCode);
		return holder.containsKey(key);
	}

	/** 言語コード指定でキャッシュホルダを返す */
	private CacheHolder<K, V> getHolder(String localeCode) {
		// その言語でキャッシュがあるか？
		// null判定を2回しているのは、synchronizedの遅延を最小にするため
		CacheHolder<K, V> holder = cache.get(localeCode);
		if (holder == null) {
			synchronized (cache) {
				holder = cache.get(localeCode);
				if (holder == null) {
					// なければその言語コードのキャッシュホルダーを作成
					final CacheManager cm = CDI.current().select(CacheManager.class).get();
					holder = cm.newInstance(this.cacheInterval);
					cache.put(localeCode, holder);
				}
			}
		}
		return holder;
	}

	public void dispose() {
		for (CacheHolder<K, V> ch : cache.values()) {
			ch.dispose();
		}
		cache.clear();
	}
}
