package jp.co.nci.iwf.component.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import jp.co.nci.iwf.component.CodeBook.CacheInterval;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.component.system.YamlMap;
import jp.co.nci.iwf.component.system.YamlService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 国際化対応の文字リソースサービス
 */
@ApplicationScoped
public class I18nService extends MiscUtils {
	/** キャッシュの同期用 */
	private static final Object sync = new Object();
	/** メッセージ内にパラメータがある場合の正規表現パターン */
	private static final Pattern PATTERN = Pattern.compile("\\{[\\d]+\\}");

	/** 現在のロケール/言語を求めるためのサービス */
	@Inject
	private Instance<LocaleService> localeResolver	;
	/** YAMLファイルの読み込みサービス */
	@Inject
	private YamlService yaml;
	/** ロガー */
	@Inject
	private Logger log;
	/** キャッシュマネージャー */
	@Inject
	private CacheManager cm;

	/** YAMLファイルのキャッシュ */
	private CacheHolder<String, Map<String, String>> caches;

	/** 初期化 */
	@PostConstruct
	public void init() {
		caches = cm.newInstance(CacheInterval.FROM_DATABASE);
	}

	/** 解放 */
	@PreDestroy
	public void destory() {
		caches.clear();
		cm.remove(caches);
	}

	/**
	 *
	 * @param localeCodes
	 */
	public void loadAndCache(Collection<String> localeCodes) {
		for (String localeCode : localeCodes) {
			readYaml(localeCode);
		}
	}

	/**
	 * メッセージCDに対するJSON用エンティティリストを返す
	 * @param messageIds
	 * @return
	 */
	public List<JsonMessage> getJsonMessages(Collection<String> messageIds) {
		if (messageIds == null)
			return new ArrayList<>();

		final String language = localeResolver.get().getLocale().getLanguage();
		final List<JsonMessage> list = messageIds.stream()
				.filter(messageCd -> isNotEmpty(messageCd))
				.distinct()
				.map(messageCd -> getJsonMessage(language, messageCd))
				.collect(Collectors.toList());
		return list;
	}

	/** メッセージCDに対するJSON用エンティティを返す */
	private JsonMessage getJsonMessage(String language, String messageCd) {
		return new JsonMessage(messageCd, getText(language, messageCd), language);
	}

	/** メッセージCDに対する文言を返す */
	public String getText(MessageCd[] messageCds) {
		// 現在のログイン者の言語コード
		final Locale locale = localeResolver.get().getLocale();
		final StringBuilder sb = new StringBuilder(64);
		for (MessageCd messageCd : messageCds) {
			sb.append(getText(locale.getLanguage(), messageCd.toString()));
		}
		return sb.toString();
	}

	/** メッセージCDに対する文言を返す */
	public String getText(MessageCd messageCd) {
		// 現在のログイン者の言語コード
		final Locale locale = localeResolver.get().getLocale();
		return getText(locale.getLanguage(), messageCd.toString());
	}

	/** メッセージCDに対する文言を返す */
	public String getText(MessageCd messageCd, Object...args) {
		// 現在のログイン者の言語コード
		final Locale locale = localeResolver.get().getLocale();
		return getText(locale.getLanguage(), messageCd.toString(), args);
	}

	/** メッセージCDに対する文言を返す */
	public String getText(MessageCd messageCd, MessageCd...args) {
		// 現在のログイン者の言語コード
		final Locale locale = localeResolver.get().getLocale();
		final List<Object> params = new ArrayList<>();
		for (MessageCd arg : args) {
			params.add(getText(arg));
		}
		return getText(locale.getLanguage(), messageCd.toString(), params.toArray());
	}

	/** メッセージCDに対する文言を返す */
	public String getText(String localeCode, MessageCd messageCd, Object...args) {
		return getText(localeCode, messageCd.toString(), args);
	}

	/** メッセージCDに対する文言を返す */
	private String getText(String language, String messageCd, Object...args) {
		if (messageCd == null)
			throw new BadRequestException("メッセージCDがNULLです");

		// 言語コードに対応したメッセージをキャッシュから求める
		final Map<String, String> cache = readYaml(language);
		String val = cache.get(messageCd);

		// 対応するメッセージがないなら、一目でリソース未定義と分かるようダミー文字列を設定
		if (val == null)
			return "%{" + messageCd + "@" + language + "}%";

		// メッセージへの埋め込みパラメータがあるなら、埋め込み処理を行う
		if (args != null && args.length > 0) {
			final Object[] params = new Object[args.length];
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				// パラメータにMessageCdが含まれていれば、対応文言へ変換
				if (arg instanceof MessageCd) {
					params[i] = getText(language, arg.toString());
				} else {
					params[i] = arg;
				}
			}
			val = format(val, params);
		}
		return val;
	}

	/**
	 * 指定ロケールのYAMLファイルを読み込み、キャッシュへ格納
	 * @param locale ロケール.
	 * @return
	 */
	private Map<String, String> readYaml(String localeCode) {
		final StopWatch sw = new StopWatch();
		sw.start();

		// まだ初期化されていなければ、YAMLファイルを読み込む
		// NULL判定を二度行っているのは、synchronizedに
		// よる遅延を最小にするための地道な努力
		Map<String, String> cache = caches.get(localeCode);
		if (cache == null) {
			synchronized(sync) {
				cache = caches.get(localeCode);
				if (cache == null) {
					// YAMLへのパス
					final String path = String.format("/i18n/message_%s.yaml", localeCode);
					final YamlMap map = yaml.read(path);
					cache = new ConcurrentHashMap<>();
					if (map != null) {
						for (final String key : map.keySet()) {
							final String value = map.getString(key);
							cache.put(key, value);
						}
					}
					log.debug("YAMLファイル[{}]からメッセージの読み込み完了 -> {}エントリ, 完了までに{}ミリ秒", path, cache.size(), sw.getTime());
				}
				caches.put(localeCode, cache);
			}
		}
		return cache;
	}

	private String format(String str, Object...args) {
		if (str != null && PATTERN.matcher(str).find() && args.length > 0) {
			try {
				return MessageFormat.format(str, args);
			}
			catch (IllegalArgumentException e) {
				return str;
			}
		}
		return str;
	}

	public boolean contains(String messageCd) {
		Locale locale = localeResolver.get().getLocale();
		return contains(locale, messageCd);
	}

	public boolean contains(Locale locale, String messageCd) {
		Map<String, String> cache = readYaml(locale.getLanguage());
		return (cache != null && cache.containsKey(messageCd));
	}
}
