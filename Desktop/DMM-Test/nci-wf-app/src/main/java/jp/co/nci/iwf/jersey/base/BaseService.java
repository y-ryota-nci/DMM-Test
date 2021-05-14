package jp.co.nci.iwf.jersey.base;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.component.menu.MenuService;
import jp.co.nci.iwf.component.system.AnnouncementService;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * サービスの基底クラス
 */
public abstract class BaseService extends MiscUtils implements CodeBook {
	/** 国際化対応サービス */
	@Inject
	protected I18nService i18n;
	/** ログイン者情報 */
	@Inject
	protected SessionHolder sessionHolder;
	/** 言語サービス */
	@Inject
	protected LocaleService localeService;
	@Inject
	private MenuService menuService;
	/** HTTPリクエスト */
	@Inject
	protected HttpServletRequest hsr;
	/** 企業属性サービス */
	@Inject
	protected CorporationPropertyService corpProp;
	/** お知らせ情報サービス */
	@Inject
	protected AnnouncementService announcement;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;

	/**
	 * レスポンス生成
	 * @param c レスポンスEntityのクラス
	 * @param req リクエストEntity
	 * @return
	 */
	protected <REQ extends BaseRequest, RES extends BaseResponse> RES createResponse(Class<RES> c, REQ req) {
		return createResponse(c, req, null);
	}

	/**
	 * レスポンス生成
	 * @param c レスポンスEntityのクラス
	 * @param req リクエストEntity
	 * @param redirectUrl リダイレクト先URL
	 * @return
	 */
	protected <REQ extends BaseRequest, RES extends BaseResponse> RES createResponse(Class<RES> c, REQ req, String redirectUrl) {
		try {
			final LoginInfo loginInfo = sessionHolder.getLoginInfo();
			final RES res = c.newInstance();
			res.success = false;
			res.redirectUrl = redirectUrl;
			res.title = getTitle();
			res.applicationName = i18n.getText(MessageCd.applicationName);

			// ログイン者情報
			res.loginInfo = loginInfo;

			// ログイン者の再ログイン可能な企業リスト
			if (loginInfo != null) {
				res.loginableCorporations = loginInfo.getLoginableCorporations();
			}

			if (req != null) {
				// 国際化対応(メッセージIDに対するJSONメッセージエンティティ)
				res.messages = i18n.getJsonMessages(req.messageCds);

				// メニュー用HTML
				if (req.needMenuHtml) {
					res.menuHtml = menuService.getMenuHtml();
					if (loginInfo != null) {
						res.userMenus = menuService.getUserMenus(loginInfo.getAccessibleMenus());
					}

					// 言語の切り替えを使用するか
					if (corpProp.getBool(CorporationProperty.USE_LANGUAGE_SWITCHING, false)) {
						res.selectableLocales = localeService.getSelectableLocaleCodeOptions(localeService.getLocaleCode());
					}
				}
				// フッター用HTML
				if (req.needFooterHtml) {
					res.footerHtml = menuService.getFooterHtml();
				}

				// APPバージョン
				if (corpProp.getBool(CorporationProperty.DISPLAY_DEBUG_INFO_ON_FOOTER, false)) {
					// デバッグ情報ありなら詳細をDB接続先や認証方法も出す
					res.appVersion = String.format("ver.%s - %s@%s auth=%s",
							manifest.getVersion(), destination.getUrl(),destination.getUser(),
							corpProp.getString(CorporationProperty.AUTHENTICATION_METHOD));
				} else {
					res.appVersion = String.format("ver.%s", manifest.getVersion());
				}
			}

			// サイト識別用の背景色と文字色
			res.siteBgColor = corpProp.getString(CorporationProperty.SITE_BG_COLOR);
			res.siteFontColor = corpProp.getString(CorporationProperty.SITE_FONT_COLOR);
			// 検証環境用の背景画像を使う
			res.useTestBgImage = corpProp.getBool(CorporationProperty.USE_TEST_BG_IMAGE, false);

			// 本日のお知らせ（自社用）
			// リソースをけちるため、ログイン直後の1回だけ表示
			if (sessionHolder != null
					&& sessionHolder.isAuthenticated()
					&& !sessionHolder.getLoginInfo().isDisplayAnnouncement()) {
				List<MwvAnnouncement> announcements = announcement.getTopAnnouncement(7);
				if (!announcements.isEmpty()) {
					res.announcements = announcements;
					res.announcementHtml = announcement.getAnnouncementHtml();
				}
				sessionHolder.getLoginInfo().setDisplayAnnouncement(true);
			}

			return res;
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new WebApplicationException(e);
		}
	}

	/**
	 * URI情報から画面タイトルを返す
	 * @param uriInfo
	 * @return
	 */
	protected String getTitle() {
		final String screenId = toScreenInfo(hsr).getScreenId();
		if (!i18n.contains(screenId)) {
			throw new BadRequestException("画面IDに対応した画面タイトルがメッセージで未定義です。screenId=" + screenId);
		}
		final MessageCd code = MessageCd.valueOf(screenId);
		final String title = i18n.getText(code);
		return String.format("%s %s", screenId, title);
	}
}
