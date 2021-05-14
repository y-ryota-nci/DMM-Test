package jp.co.nci.iwf.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * 画面ルックアップマスタのサービス
 */
@BizLogic
public class MwmLookupService extends BaseRepository implements CodeBook {
	private static final String[] NO_FILTER = new String[0];

	@Inject private SessionHolder sessionHolder;
	@Inject private CacheManager cm;

	/** キャッシュ */
	private CacheHolder<String, MwmLookup> cache;
	private CacheHolder<String, List<MwmLookup>> cacheList;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cache = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		cacheList = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/** リソース解放 */
	@PreDestroy
	public void dispose() {
		cache.dispose();
		cacheList.dispose();
	}

	/**
	 * ルックアップグループIDをキーに、該当する全ルックアップをOptionItemリスト形式で抽出
	 * @param lookupGroupId ルックアップグループID
	 * @param emptyLine 先頭に空行を挿入するならtrue
	 * @return
	 */
	public List<OptionItem> getOptionItems(LookupGroupId lookupGroupId, boolean emptyLine) {
		return getOptionItems(lookupGroupId, emptyLine, new String[0]);
	}

	/**
	 * ルックアップグループIDをキーに、ルックアップIDに該当するものだけをフィルターしてOptionItemリスト形式で抽出
	 * @param lookupGroupId ルックアップグループID
	 * @param emptyLine 先頭に空行を挿入するならtrue
	 * @param lookupIds ルックアップIDのフィルター。nullまたは空要素ならフィルターしない。
	 * @return
	 */
	public List<OptionItem> getOptionItems(LookupGroupId lookupGroupId, boolean emptyLine, Collection<String> lookupIds) {
		return getOptionItems(lookupGroupId, emptyLine, lookupIds.toArray(new String[lookupIds.size()]));
	}

	/**
	 * ルックアップグループIDをキーに、ルックアップIDに該当するものだけをフィルターしてOptionItemリスト形式で抽出
	 * @param lookupGroupId ルックアップグループID
	 * @param emptyLine 先頭に空行を挿入するならtrue
	 * @param lookupIds ルックアップIDのフィルター。nullまたは空要素ならフィルターしない。
	 * @return
	 */
	public List<OptionItem> getOptionItems(LookupGroupId lookupGroupId, boolean emptyLine, String...lookupIds) {
		final List<OptionItem> options = new ArrayList<>();
		if (emptyLine)
			options.add(OptionItem.EMPTY);

		final List<MwmLookup> lookups = get(lookupGroupId, lookupIds);
		for (MwmLookup lookup : lookups) {
			options.add(new OptionItem(lookup.getLookupId(), lookup.getLookupName()));
		}
		return options;
	}

	/**
	 * ルックアップグループIDをキーに、ルックアップIDに該当するものだけをフィルターしてOptionItemリスト形式で抽出
	 * @param lookupGroupId ルックアップグループID
	 * @param emptyLine 先頭に空行を挿入するならtrue
	 * @param lookupIds ルックアップIDのフィルター。nullまたは空要素ならフィルターしない。
	 * @return
	 */
	public List<OptionItem> getOptionItems(LookupGroupId lookupGroupId, boolean emptyLine, Enum<?>...lookupIds) {
		return getOptionItems(lookupGroupId, emptyLine, toStrings(lookupIds));
	}

	/**
	 * ルックアップグループIDをキーに抽出
	 * @param localeCode
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public List<MwmLookup> get(String localeCode, LookupGroupId lookupGroupId) {
		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		return get(localeCode, corporationCode, lookupGroupId, NO_FILTER);
	}
	/**
	 * ルックアップグループIDをキーに抽出
	 * @param localeCode
	 * @param corporationCode
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public List<MwmLookup> get(String localeCode, String corporationCode, LookupGroupId lookupGroupId) {
		return get(localeCode, corporationCode, lookupGroupId, NO_FILTER);
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param localeCode 言語コード
	 * @param corporationCode 企業コード
	 * @param lookupGroupId ルックアップグループID
	 * @param lookupId ルックアップIDの絞り込み条件
	 * @return
	 */
	public MwmLookup get(String localeCode, String corporationCode, LookupGroupId lookupGroupId, String lookupId) {
		// キャッシュにあるか？
		final String key = toKey(localeCode, corporationCode, lookupGroupId, lookupId);
		MwmLookup lookup = cache.get(key);
		if (lookup != null)
			return lookup;

		// なければ抽出
		final Object[] params = { corporationCode, lookupGroupId.toString(), lookupId, localeCode };
		lookup = selectOne(MwmLookup.class, getSql("CM0008"), params);

		// キャッシュへ
		if (lookup != null)
			cache.put(key, lookup);
		return lookup;
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param localeCode 言語コード
	 * @param corporationCode 企業コード
	 * @param lookupGroupId ルックアップグループID
	 * @param lookupIds ルックアップIDの絞り込み条件
	 * @return
	 */
	public List<MwmLookup> get(String localeCode, String corporationCode, LookupGroupId lookupGroupId, String...lookupIds) {
		// キャッシュにあるか？
		final String key = toKey(localeCode, corporationCode, lookupGroupId);
		List<MwmLookup> lookups = cacheList.get(key);
		if (lookups == null) {
			// なければ抽出
			final Object[] params = { corporationCode, lookupGroupId.toString(), localeCode };
			lookups = select(MwmLookup.class, getSql("CM0009"), params);

			// キャッシュへ
			cacheList.put(key, lookups);
		}
		// ルックアップIDが指定されていれば、指定されている者だけをピックアップ
		if (lookupIds.length == 0) {
			return lookups;
		}
		final Set<String> filters = asSet(lookupIds);
		return lookups.stream()
				.filter(e -> filters.contains(e.getLookupId()))
				.collect(Collectors.toList());
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param localeCode 言語コード
	 * @param corporationCode 企業コード
	 * @param lookupGroupId ルックアップグループID
	 * @param lookupIds ルックアップIDの絞り込み条件
	 * @return
	 */
	public List<MwmLookup> get(String localeCode, String corporationCode, LookupGroupId lookupGroupId, Enum<?>...lookupIds) {
		return get(localeCode, corporationCode, lookupGroupId, toStrings(lookupIds));
	}

	/**
	 * ルックアップグループIDをキーに抽出
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public List<MwmLookup> get(LookupGroupId lookupGroupId) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String coporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		return get(localeCode, coporationCode, lookupGroupId, NO_FILTER);
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param lookupGroupId
	 * @return
	 */
	public MwmLookup get(LookupGroupId lookupGroupId, String lookupId) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		return get(localeCode, corporationCode, lookupGroupId, lookupId);
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param lookupGroupId
	 * @return
	 */
	public List<MwmLookup> get(LookupGroupId lookupGroupId, String... lookupIds) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		return get(localeCode, corporationCode, lookupGroupId, lookupIds);
	}

	/**
	 * ルックアップグループID＋ルックアップIDをキーに抽出
	 * @param lookupGroupId
	 * @return
	 */
	public List<MwmLookup> get(LookupGroupId lookupGroupId, Enum<?>... lookupIds) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		return get(localeCode, corporationCode, lookupGroupId, toStrings(lookupIds));
	}

	/**
	 * ルックアップグループIDをキーに抽出し、ルックアップIDをキーとしたMapとして返す
	 * @param localeCode
	 * @param corporationCode
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public Map<String, MwmLookup> getMap(String localeCode, String corporationCode, LookupGroupId lookupGroupId) {
		return get(localeCode, corporationCode, lookupGroupId)
				.stream()
				.collect(Collectors.toMap(MwmLookup::getLookupId, p -> p));
	}

	/**
	 * ルックアップグループIDをキーに抽出し、ルックアップIDをキーとしたMapとして返す
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public Map<String, MwmLookup> getMap(LookupGroupId lookupGroupId) {
		return get(lookupGroupId)
				.stream()
				.collect(Collectors.toMap(MwmLookup::getLookupId, p -> p));
	}


	/**
	 * ルックアップグループIDをキーに抽出し、ルックアップIDをキーとした名称Mapとして返す
	 * @param lookupGroupId ルックアップグループID
	 * @return
	 */
	public Map<String, String> getNameMap(LookupGroupId lookupGroupId) {
		return get(lookupGroupId)
				.stream()
				.collect(Collectors.toMap(MwmLookup::getLookupId, MwmLookup::getLookupName));
	}

	/**
	 * ルックアップ名称を抽出
	 * @param lookupGroupId ルックアップグループID
	 * @param lookupId ルックアップID
	 * @return
	 */
	public String getName(LookupGroupId lookupGroupId, String lookupId) {
		final MwmLookup lookup = get(lookupGroupId, lookupId);
		return (lookup == null ? null : lookup.getLookupName());
	}

	/** キャッシュ用キーの生成 */
	private String toKey(String localeCode, String corporationCode, LookupGroupId lookupGroupId, String lookupId) {
		return new StringBuilder()
				.append(corporationCode).append("/")
				.append(lookupGroupId).append("/")
				.append(lookupId).append("@")
				.append(localeCode)
				.toString();
	}

	/** キャッシュ用キーの生成 */
	private String toKey(String localeCode, String corporationCode, LookupGroupId lookupGroupId) {
		return new StringBuilder()
				.append(corporationCode).append("/")
				.append(lookupGroupId).append("@")
				.append(localeCode)
				.toString();
	}

	/** Enumの可変長配列を文字配列に変換 */
	private String[] toStrings(Enum<?>...enums) {
		final String[] values = new String[enums.length];
		for (int i = 0; i < enums.length; i++)
			values[i] = enums[i].toString();
		return values;
	}
}
