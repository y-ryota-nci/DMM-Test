package jp.co.nci.iwf.component.system;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfvCorporationProperty;

/**
 * 企業プロパティ・サービス
 */
@ApplicationScoped
public class CorporationPropertyService extends BaseRepository {

	/** 同期用 */
	private static final Object sync = new Object();

	/** キャッシュマネージャ */
	@Inject private CacheManager cm;
	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;
	/** 言語サービス */
	@Inject private LocaleService locale;
	/** 各企業の独自プロパティを格納したキャッシュ */
	private CacheHolder<String, Map<String, String>> cache;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cache = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/** リソース解放 */
	@PreDestroy
	public void destory() {
		clearCache();
		cm.remove(cache);
	}

	/** プロパティ値をIntegerで返す */
	public Integer getInt(CorporationProperty corporationProperty) {
		String s = get(corporationProperty);
		return toInt(s);
	}

	/** プロパティ値をIntegerで返す */
	public int getInt(CorporationProperty corporationProperty, int defaultValue) {
		final String s = get(corporationProperty);
		final Integer value = toInt(s);
		return defaults(value, defaultValue);
	}

	/** プロパティ値をLongで返す */
	public Long getLong(CorporationProperty corporationProperty) {
		String s = get(corporationProperty);
		return toLong(s);
	}

	/** プロパティ値をLongで返す */
	public long getLong(CorporationProperty corporationProperty, long defaultValue) {
		final Long v = toLong(corporationProperty);
		return defaults(v, defaultValue);
	}

	/** プロパティ値をDoubleで返す */
	public Double getDouble(CorporationProperty corporationProperty) {
		String s = get(corporationProperty);
		return toDouble(s);
	}

	/** プロパティ値をBigDecimalで返す */
	public BigDecimal getBigDecimal(CorporationProperty corporationProperty) {
		String s = get(corporationProperty);
		return toBD(s);
	}

	/** プロパティ値をBooleanで返す */
	public Boolean getBool(CorporationProperty corporationProperty) {
		String s = get(corporationProperty);
		return toBool(s);
	}

	/** プロパティ値をBooleanで返す */
	public boolean getBool(CorporationProperty corporationProperty, boolean defaultValue) {
		String s = get(corporationProperty);
		Boolean b = toBool(s);
		return (b == null) ? defaultValue : b.booleanValue();
	}

	/** プロパティ値を文字列で返す */
	public String getString(CorporationProperty corporationProperty) {
		return get(corporationProperty);
	}

	/** プロパティ値を取得 */
	private String get(CorporationProperty corporationProperty) {
		final String key = corporationProperty.toString();
		final LoginInfo login = sessionHolder.getLoginInfo();
		// 企業コードが定まらないならASPとしておく
		final String corporationCode;
		if (login == null || isEmpty(login.getCorporationCode()))
			corporationCode = CorporationCode.ASP;
		else
			corporationCode = login.getCorporationCode();

		// キャッシュにあるか
		Map<String, String> props = cache.get(corporationCode);
		if (props == null) {
			synchronized (sync) {
				props = cache.get(corporationCode);
				if (props == null) {
					props = loadCorpProperties(corporationCode);
					cache.put(corporationCode, props);
				}
			}
		}
		if (props != null) {
			return trim(props.get(key));
		}
		return null;
	}

	/** 企業独自のプロパティをロード */
	private Map<String, String> loadCorpProperties(String corporationCode) {
		final Object[] params = { corporationCode, locale.getLocaleCode() };
		return select(WfvCorporationProperty.class, getSql("CM0002"), params)
				.stream()
				.collect(Collectors.toMap(
						WfvCorporationProperty::getPropertyCode,
						WfvCorporationProperty::getPropertyValue,
						(a, b) -> b));		// 同一キーがあれば後勝ちとする
	}

	/** キャッシュのクリア */
	public void clearCache() {
		cache.clear();
	}
}
