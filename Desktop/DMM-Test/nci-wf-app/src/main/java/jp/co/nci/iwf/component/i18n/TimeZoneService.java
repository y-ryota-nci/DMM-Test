package jp.co.nci.iwf.component.i18n;

import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;

/**
 * タイムゾーンサービス
 */
@ApplicationScoped
public class TimeZoneService {
	@Inject
	private SessionHolder sessionHolder;
	/** デフォルトタイムゾーン */
	private static final TimeZone defaultTimeZone = TimeZone.getTimeZone("Asia/Tokyo");

	/** ログイン者のタイムゾーンを求める */
	public TimeZone resolveTimeZone() {
		// ログイン者情報
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		if (loginInfo != null && loginInfo.getTimeZone() != null)
			return TimeZone.getTimeZone(loginInfo.getTimeZone());

		return defaultTimeZone;
	}

	/** ログイン者のタイムゾーンを返す */
	public String getTimeZone() {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		if (loginInfo != null && StringUtils.isNotEmpty(loginInfo.getTimeZone()))
			return loginInfo.getTimeZone();

		return defaultTimeZone.getDisplayName();
	}
}
