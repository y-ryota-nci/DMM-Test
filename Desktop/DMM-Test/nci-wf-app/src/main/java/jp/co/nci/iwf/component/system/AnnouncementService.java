package jp.co.nci.iwf.component.system;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.HtmlResourceService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * お知らせサービス
 */
@BizLogic
public class AnnouncementService extends BaseService {
	@Inject private AnnouncementReposiroty repository;
	@Inject private LocaleService locale;
	/** お知らせ表示用HTMLのキャッシュ */
	@Inject private HtmlResourceService htmlCache;

	/** 「本日のお知らせ」の置換パターン */
	private static final String PATTERN = MiscUtils.quotePattern("###{announcementOfTheDay}###");

	/**
	 * 【未ログイン】ログイン画面用のお知らせを抽出して返す
	 * @param maxCount 最大取得件数
	 * @return
	 */
	public List<MwvAnnouncement> getLoginAnnouncement(int maxCount) {
		String localeCode = locale.getLocaleCode();	// ログイン前なので、SessionHolderから取るのは誤り
		Timestamp baseDate = timestamp();
		return modify(repository.getLoginAnnouncement(baseDate, localeCode, maxCount));
	}

	/**
	 * 【ログイン後】ログイン者が閲覧可能なお知らせを抽出して返す
	 * @param maxCount 最大取得件数
	 * @return
	 */
	public List<MwvAnnouncement> getTopAnnouncement(int maxCount) {
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		Timestamp baseDate = timestamp();
		return modify(repository.getTopAnnouncement(corporationCode, baseDate, localeCode, maxCount));
	}

	/** 表示用にお知らせ情報を補完 */
	private List<MwvAnnouncement> modify(List<MwvAnnouncement> list) {
		Date to = today();
		Date from = addDay(to, -3);
		list.forEach(e -> {
			// 公開日(有効開始日の時分秒を切捨て）
			e.publicationDate = new java.sql.Date(e.timestampStart.getTime());
			// 'NEW'ラベル
			if (between(e.timestampStart, from, to)) {
				e.labelNew = "NEW";
			}
		});
		return list;
	}

	/**
	 * お知らせ表示用HTML文字列を返す
	 * @return
	 */
	public String getAnnouncementHtml() {
		String localeCode = locale.getLocaleCode();
		String html = htmlCache.getContents("announcement.html");
		if (html != null) {
			html = html.replaceFirst(PATTERN, i18n.getText(localeCode, MessageCd.announcementOfTheDay));
		}
		return html;
	}
}
