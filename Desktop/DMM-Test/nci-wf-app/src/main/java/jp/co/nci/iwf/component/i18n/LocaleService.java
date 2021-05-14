package jp.co.nci.iwf.component.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.LocaleCode;
import jp.co.nci.iwf.component.CodeBook.CacheInterval;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.wm.WfmLocaleV;

/**
 * 操作者の多言語対応用のロケール解決サービス
 */
@ApplicationScoped
public class LocaleService {

	/** 同期用 */
	private final Object sync = new Object();
	/** デフォルトロケール */
	public static final Locale DEFAULT_LOCALE = Locale.JAPAN;

	/**
	 * 仮の言語一覧。ApplicationContextListenerなどリクエスト処理前に言語一覧を求められたとき用。
	 * Listenerではスレッドの外側なので、SessionScopeやRequestScopeのDIが出来ないため、決め打ちする。
	 * 用途が初期化中の対象言語選定なので、それほど厳密性は求められないため、これで良い。
	 */
	public static final Set<String> INIT_LOCALE_CODES =
			new HashSet<>(Arrays.asList(LocaleCode.JP, LocaleCode.EN, LocaleCode.ZH));

	@Inject
	private Instance<SessionHolder> ish;
	@Inject
	private LocaleRepository repository;
	@Inject
	private Instance<HttpServletRequest> ihsr;
	@Inject
	private CacheManager cm;

	/** 表示言語別のロケールマスタのキャッシュ */
	private CacheHolder<String, List<WfmLocaleV>> cacheLocales;
	/** 選択可能な言語一覧のキャッシュ */
	private CacheHolder<String, List<OptionItem>> cacheOptions;
	/** 選択可能な言語リスト（WFM_LOCALEからの読み込み値から生成される） */
	private Set<String> selectableLocaleCodes;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cacheLocales = cm.newInstance(CacheInterval.FOREVER);
		cacheOptions = cm.newInstance(CacheInterval.FOREVER);
	}

	/** リソース解放 */
	@PreDestroy
	public void destory() {
		cacheLocales.clear();
		cacheOptions.clear();
	}

	/** ログイン者のロケール（＝国＋言語）を求める */
	public Locale getLocale() {
		// ログイン者情報
		final SessionHolder sh = ish.get();
		if (sh != null) {
			final LoginInfo loginInfo = sh.getLoginInfo();
			if (loginInfo != null
					&& StringUtils.isNotEmpty(loginInfo.getLocaleCode())
					&& isSelectableLocaleCode(loginInfo.getLocaleCode())
			) {
				return new Locale(loginInfo.getLocaleCode());
			}
		}
		// ブラウザの言語設定
		if (ihsr != null) {
			final HttpServletRequest req = ihsr.get();
			if (req != null && req.getLocale() != null) {
				final String language = req.getLocale().getLanguage();
				if (isSelectableLocaleCode(language))
					return req.getLocale();
			}
		}
		return DEFAULT_LOCALE;
	}

	/**
	 * ログイン者のロケールから言語を求めて返す。
	 * @return "ja"などの言語
	 */
	public String getLocaleCode() {
		// ログイン者情報
		final SessionHolder sh = ish.get();
		if (sh != null) {
			final LoginInfo loginInfo = sh.getLoginInfo();
			if (loginInfo != null
					&& StringUtils.isNotEmpty(loginInfo.getLocaleCode())
					&& isSelectableLocaleCode(loginInfo.getLocaleCode())
			) {
				return loginInfo.getLocaleCode();
			}
		}
		// ブラウザの言語設定
		if (ihsr != null) {
			final HttpServletRequest req = ihsr.get();
			if (req != null && req.getLocale() != null) {
				final String language = req.getLocale().getLanguage();
				if (isSelectableLocaleCode(language))
					return language;
			}
		}
		return DEFAULT_LOCALE.getLanguage();
	}

	/**
	 * 有効な言語か
	 * @param newLocaleCode
	 * @return
	 */
	public boolean isSelectableLocaleCode(String newLocaleCode) {
		return StringUtils.isNotEmpty(newLocaleCode)
				&& getSelectableLocaleCodes().contains(newLocaleCode);
	}

	/**
	 * 選択可能な言語コードを返す
	 * @return
	 */
	public Set<String> getSelectableLocaleCodes() {
		if (selectableLocaleCodes == null) {
			synchronized (sync) {
				if (selectableLocaleCodes == null) {
					// 全言語リストを抽出(言語名称は日本語表記だが、必要なのは言語コードだけなので問題ない)
					selectableLocaleCodes = getWfmLocale(DEFAULT_LOCALE.getLanguage())
							.stream()
							.map(e -> e.getLocaleCode())
							.collect(Collectors.toSet());
				}
			}
		}
		return selectableLocaleCodes;
	}

	/**
	 * 言語マスタ一覧を返す
	 * @param displayLocaleCode 言語コード(何語でロケール名を表示するかの判定用)
	 * @return
	 */
	public List<WfmLocaleV> getWfmLocale(String displayLocaleCode) {
		List<WfmLocaleV> locales = cacheLocales.get(displayLocaleCode);
		if (locales == null) {
			synchronized (sync) {
				locales = cacheLocales.get(displayLocaleCode);
				if (locales == null) {
					locales = repository.getAll(displayLocaleCode);
					cacheLocales.put(displayLocaleCode, locales);
				}
			}
		}
		return locales;
	}

	/**
	 * 選択可能な言語をJSONで解釈可能な形式で返す
	 * @return
	 */
	public List<OptionItem> getSelectableLocaleCodeOptions() {
		final String lang = getLocaleCode();
		return getSelectableLocaleCodeOptions(lang);
	}

	/**
	 * 選択可能な言語をJSONで解釈可能な形式で返す
	 * @param lang 言語コード
	 * @param emptyLine 先頭に空行を挿入するならtrue。
	 * @return
	 */
	public List<OptionItem> getSelectableLocaleCodeOptions(String lang) {
		return getSelectableLocaleCodeOptions(lang, false);
	}

	/**
	 * 選択可能な言語をJSONで解釈可能な形式で返す
	 * @param emptyLine 先頭に空行を挿入するならtrue。
	 * @return
	 */
	public List<OptionItem> getSelectableLocaleCodeOptions(boolean emptyLine) {
		final String lang = getLocaleCode();
		return getSelectableLocaleCodeOptions(lang, emptyLine);
	}

	/**
	 * 選択可能な言語をJSONで解釈可能な形式で返す
	 * @param lang 言語コード
	 * @param emptyLine 先頭に空行を挿入するならtrue。
	 * @return
	 */
	public List<OptionItem> getSelectableLocaleCodeOptions(String lang, boolean emptyLine) {
		// NULL判定を2回しているのは synchronizedのコストを最小にするための不断の努力
		List<OptionItem> options = cacheOptions.get(lang);
		if (options == null) {
			synchronized(sync) {
				options = cacheOptions.get(lang);
				if (options == null) {
					// ロケールマスタ一覧を OptionItemリストへ変換し、キャッシュ
					options = getWfmLocale(lang)
							.stream()
							.map(e -> new OptionItem(e.getLocaleCode(), e.getLocaleName()))
							.collect(Collectors.toList());
					cacheOptions.put(lang, options);
				}
			}
		}
		if (emptyLine) {
			List<OptionItem> clone = new ArrayList<>(options);
			clone.add(0, OptionItem.EMPTY);
			return clone;
		}
		return options;
	}
}
