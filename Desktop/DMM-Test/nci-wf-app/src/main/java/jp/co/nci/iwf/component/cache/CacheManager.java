package jp.co.nci.iwf.component.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * キャッシュ管理サービス
 */
@ApplicationScoped
public class CacheManager extends BaseRepository {
//	@Inject private Logger log;

	/** 10秒＝10秒 * 1000ミリ秒 */
	public static final long TEN_SECONDS = TimeUnit.SECONDS.toMillis(10);
	/** 管理対象のキャッシュ */
	private transient ConcurrentMap<Integer, CacheHolder<?, ?>> instances = new ConcurrentHashMap<>();
	/** キャッシュ＝環境依存の場合のキャッシュタイムアウト時間（ミリ秒） */
	private transient long timeout;
	/** 最後にキャッシュを読み直した時間 */
	private transient long timestamp;
	/** キャッシュID採番機 */
	private transient AtomicInteger ids = new AtomicInteger(0);

	// 通常なら SqlManager経由でYAMLファイルからSQL分を取り出すのがスジである。
	// しかしながら、SqlService自体がCacheManagerの設定に依存しているため、
	// CacheManager内でSqlManagerを呼び出すと循環参照となってしまう。
	 // これを回避するために直接SQL文を管理している
	private static final String SQL =
			"select /* CM0000 */ * from WFM_CORP_PROP_MASTER where PROPERTY_CODE = ?";

	/** 初期化 */
	@PostConstruct
	public synchronized void init() {
		// キャッシュを読み直した時間を記録
		timestamp = System.currentTimeMillis();

		// 処理前のタイムアウト時間を退避
		long old = timeout;

		// キャッシュ＝環境依存のキャッシュ間隔はシステムプロパティから求める
		final Object[] params = { CorporationProperty.CACHE_INTERVAL_SECONDS.toString() };
		final WfmCorpPropMaster prop = selectOne(WfmCorpPropMaster.class, SQL, params);
		if (prop != null) {
			if (ValidatorUtils.isInteger(prop.getDefaultValue()))
				timeout = defaults(toLong(prop.getDefaultValue()), 0L) * 1000L;	// 秒→ミリ秒変換
			else
				timeout = 0L;
		} else {
			timeout = 0L;
		}

		// 処理前とタイムアウトが変わっていたら管理下にあるキャッシュのタイムアウト時間を変更。
		// （これによって既存のキャッシュは期限切れとなる可能性がある）
//		log.debug("oldTimeout={} newTimeout={}", old, timeout);
		if (timeout != old) {
			for (CacheHolder<?, ?> ch : instances.values()) {
				if (ch.isChangeableTimeout() && ch.getTimeout() != timeout) {
					ch.setTimeout(timeout);
				}
			}
		}
	}

	/** リソース解放 */
	@PreDestroy
	public void dispose() {
		for (CacheHolder<?, ?> ch : instances.values()) {
			ch.dispose();
		}
		instances.clear();
	}

	/**
	 * 新しいキャッシュインスタンスを返す。
	 *
	 * @param cacheInterval キャッシュ値を保持する時間の定数。
	 * 		EVERY_10SECONDS は運用中に変更があり得る用途に。
	 * 		FROM_DATABASEなら「開発中はXX秒だが本番なら不変」のような環境依存の用途に。
	 * 		FOREVERなら不変な要素に。
	 * @return キャッシュホルダーのインスタンス
	 */
	public <K, T> CacheHolder<K, T> newInstance(CacheInterval cacheInterval) {
		final CacheHolder<K, T> ch;
		switch (cacheInterval) {
		case EVERY_10SECONDS:
			// 10秒毎に読み直す
			ch = new CacheHolder<K, T>(TEN_SECONDS, cacheInterval, ids.addAndGet(1));
			break;
		case FROM_DATABASE:
			// キャッシュ＝DB依存なら初期化時に定めた秒数ごとに読み直す
			// @see CacheManager::init()
			ch = new CacheHolder<K, T>(timeout, cacheInterval, ids.addAndGet(1));
			break;
		case FOREVER:
			// キャッシュ＝永遠なら二度と読み直さない
			ch = new CacheHolder<K, T>(0L, cacheInterval, ids.addAndGet(1));
			break;
		default:
			throw new IllegalArgumentException("interval=" + cacheInterval);
		}
		instances.put(ch.getId(), ch);
		return ch;
	}

	/**
	 * 新しい言語別キャッシュインスタンスを返す。
	 *
	 * @param cacheInterval キャッシュ値を保持する時間の定数。
	 * 		EVERY_10SECONDS は運用中に変更があり得る用途に。
	 * 		FROM_DATABASEなら「開発中はXX秒だが本番なら不変」のような環境依存の用途に。
	 * 		FOREVERなら不変な要素に。
	 * @return キャッシュホルダーのインスタンス
	 */
	public <K, T> LocaledCacheHolder<K, T> newLocaledInstance(CacheInterval cacheInterval) {
		LocaledCacheHolder<K, T> holder = new LocaledCacheHolder<>(cacheInterval);
		return holder;

	}

	/** 最後にキャッシュを読み込んだ時刻を返す */
	public long getTimestamp() {
		return this.timestamp;
	}

	/** キャッシュ削除 */
	public CacheHolder<?, ?> remove(CacheHolder<?, ?> ch) {
		if (ch == null)
			return null;
		return remove(ch.getId());
	}

	/** キャッシュ削除 */
	public CacheHolder<?, ?> remove(int id) {
		return instances.remove(id);
	}

	/** キャッシュIDをキーにキャッシュを返す */
	public CacheHolder<?, ?> get(int id) {
		return instances.get(id);
	}
}
