package jp.co.nci.iwf.component.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import jp.co.nci.iwf.component.CodeBook.CacheInterval;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * SQL取得サービス
 */
@ApplicationScoped
public class SqlService extends MiscUtils {
	/** SQLを記述したYAMLファイルのファイル名リスト */
	private static final String[] fileNames = {
			"sql_mm.yaml",
			"sql_au.yaml",
			"sql_cm.yaml",
			"sql_vd.yaml",
			"sql_na.yaml",
			"sql_al.yaml",
			"sql_rm.yaml",
			"sql_ti.yaml",
			"sql_up.yaml",
			"sql_md.yaml",
			"sql_mg.yaml",
			"sql_ml.yaml",
			"sql_wl.yaml",
			"sql_dc.yaml",
			"sql_ct.yaml",	// DMMカタログ
			"sql_py.yaml",	// DMM支払依頼
			"sql_ri.yaml",	// DMM検収
			"sql_po.yaml",	// DMM発注
			"sql_sp.yaml",	// DMM取引先
			"sql_co.yaml",	// DMM契約
			"sql_sg.yaml",	// DMMサジェスチョン
			"sql_pr.yaml",	// DMM購入依頼
			"sql_ap.yaml",	// DMM前払金
			"sql_ss.yaml",	// DMM SS連携
			"sql_bd.yaml",	// DMM予算
	};

	/** キャッシュマネージャ */
	@Inject private CacheManager cm;
//	/** スレッドプールサービス */
//	@Inject private ThreadPoolService pool;
	/** ロガー */
	@Inject private Logger log;
	/** YAMLファイルサービス */
	@Inject private YamlService yaml;


	/** キャッシュ */
	private Map<String, CacheHolder<String, String>> map = new HashMap<>(32);

	/** 初期化 */
	@PostConstruct
	public synchronized void init() {
		for (String fileName : fileNames) {
			CacheHolder<String, String> cache = cm.newInstance(CacheInterval.FROM_DATABASE);
			map.put(fileName, cache);
		}
	}

	/** 解放 */
	@PreDestroy
	public void destory() {
		clear();
		for (CacheHolder<String, String> cache : map.values()) {
			cache.dispose();
			cm.remove(cache);
		}
	}

	/** 既存キャッシュクリア */
	public void clear() {
		for (CacheHolder<String, String> cache : map.values()) {
			cache.clear();
		}
	}

	/**
	 * キーをもとに、YAMLファイルからSQLを取得
	 * @param id
	 * @return
	 */
	public String get(String id) {
		// SQL IDの先頭2バイトがYAMLファイルのサフィックスとなる
		final String prefix = id.substring(0, 2).toLowerCase();
		final String fileName = "sql_" + prefix.toLowerCase() + ".yaml";

		final CacheHolder<String, String> cache = map.get(fileName);
		if (cache == null) {
			throw new InternalServerErrorException("SQL ID [" + id + "] に対応した YAMLファイル [" + fileName + "]が存在しません。");
		}
		// ロックなしでSQL取得を試みる（高速）
		String sql = cache.get(id);
		if (sql == null)
			// ロックありでSQL取得を試みる（低速だが他スレッドの処理待ちしたうえで判定するので、アトミックである）
			synchronized(cache) {
				sql = cache.get(id);
				if (sql == null) {
					// ロックしてもなおSQLを取得できなければ、YAMLファイルを読み直す（最低速）
					reload(fileName, cache);
					sql = cache.get(id);
				}
			}
		// キャッシュからSQLを取得。
		return sql;
	}

	/**
	 * 既存キャッシュをクリアして、SQLをYAMLファイルから再読込＆キャッシュ
	 */
	public void loadAndCache() {
		clear();
		for (String fileName : fileNames) {
			CacheHolder<String, String> cache = map.get(fileName);
			synchronized (cache) {
				reload(fileName, cache);
			}
		}
	}

	/** SQLをYAMLファイルから読み込んでキャッシュ */
	private void reload(String fileName, CacheHolder<String, String> cache) {
		final StopWatch sw = new StopWatch();
		sw.start();

		final String path = "/sql/" + fileName;
		YamlMap yamls = yaml.read(path);
		for (Entry<String, Object> entry : yamls.entrySet()) {
			cache.put(entry.getKey(), entry.getValue().toString());
		}

		log.debug("YAMLファイル[{}]からSQLの読み込み完了 -> {}エントリ, 完了までに{}ミリ秒", fileName, cache.size(), sw.getTime());
	}
}
