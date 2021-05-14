package jp.co.nci.iwf.component;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenuScreenExclude;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 画面単位のアクセス権限サービス
 */
@ApplicationScoped
public class ScreenAuthorityService extends BaseRepository implements CodeBook {
	/** キャッシュキー */
	private static final String KEY = "KEY";

	/** キャッシュマネージャー */
	@Inject private CacheManager cm;

	/** キャッシュ */
	private CacheHolder<String, Pattern> holder;

	/** ロック用Object */
	private final transient Object sync = new Object();

	@PostConstruct
	public void init() {
		holder = cm.newInstance(CacheInterval.FROM_DATABASE);
	}

	/**
	 * 対象URIに対してアクセス権があるか
	 * @param crc
	 * @return
	 */
	public boolean isAuthorized(LoginInfo login, HttpServletRequest req) {
		// ログイン前は不可
		if (login == null)
			return false;
		final List<MwmMenu> menus = login.getAccessibleMenus();
		if (menus == null || menus.isEmpty())	// メニューがないならログイン前
			return false;

		// これからアクセスしようとしている画面ID
		final String screenId = getScreenId(req);
		if (isEmpty(screenId))
			return true;	// 画面IDがNULLということは、たぶんコンテキストルートへのアクセスだ

		// アクセス可能な画面が１つも無ければ、画面単位のアクセス制御をしないことを示す
		final Set<String> accessibles = login.getAccessibleScreenIds();
		if (accessibles == null || accessibles.isEmpty())
			return true;

		// 画面IDに対してアクセス権があるか、アクセス権の不要な画面なら認可されているとみなす
		final Pattern ignore = getExcludePattern();
		return accessibles.contains(screenId)
				|| (ignore != null && ignore.matcher(screenId).find());
	}

	/** HTTPリクエストから画面IDを抜き出す */
	private String getScreenId(HttpServletRequest req) {
		return MiscUtils.toScreenInfo(req).getScreenId();
	}

	/** アクセス権判定から除外する画面IDの正規表現パターンを抽出 */
	private Pattern getExcludePattern() {
		// キャッシュにあるか？
		// null判定を二回行っているのはsynchronizedによるパフォーマンス低下を回避するためだ
		Pattern pattern = holder.get(KEY);
		if (pattern == null) {
			synchronized(sync) {
				pattern = holder.get(KEY);
				if (pattern == null) {
					// 除外パターンを抽出し、すべて文字連結して正規表現パターン化
					String sql = getSql("AU0009");
					String exclude = select(MwmMenuScreenExclude.class, sql).stream()
							.filter(mse -> isNotEmpty(mse.getExcludePattern()))
							.map(mse -> "^" + mse.getExcludePattern() + "$")
							.collect(Collectors.joining("|"));
					pattern = Pattern.compile(exclude);
					holder.put(KEY, pattern);
				}
			}
		}
		return pattern;
	}
}
