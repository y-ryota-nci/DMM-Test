package jp.co.nci.iwf.jersey.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.i18n.JsonMessage;
import jp.co.nci.iwf.component.menu.MenuEntry;
import jp.co.nci.iwf.jpa.entity.ex.MwvAnnouncement;

/**
 * レスポンスの基底クラス
 */
public class BaseResponse implements Serializable, IResponse {
	/** リクエスト成否 */
	public boolean success = true;
	/** ログイン者情報 */
	public LoginInfo loginInfo;
	/** リクエストされたメッセージIDリストに対するJSONメッセージ一覧 */
	public List<JsonMessage> messages;
	/** リダイレクト先URL */
	public String redirectUrl;
	/** 画面タイトル（BaseService.createResponse()で自動設定） */
	public String title;
	/** APP名（BaseService.createResponse()で自動設定） */
	public String applicationName;
	/** APPバージョン */
	public String appVersion;
	/** エラーメッセージ */
	public List<String> alerts = new ArrayList<>();
	/** 情報メッセージ */
	public List<String> successes = new ArrayList<>();
	/** 警告メッセージ */
	public List<String> warns = new ArrayList<>();
	/** メニューHTML */
	public String menuHtml;
	/** フッターHTML */
	public String footerHtml;
	/** 選択可能な言語 */
	public List<OptionItem> selectableLocales;
	/** メニューデータ */
	public List<MenuEntry> userMenus;
	/** サイト識別用の背景色 */
	public String siteBgColor;
	/** サイト識別用の文字色 */
	public String siteFontColor;
	/** （自社の）お知らせ情報 */
	public List<MwvAnnouncement> announcements;
	/** お知らせ表示用HTML */
	public String announcementHtml;
	/** 検証環境用の背景画像を使う */
	public boolean useTestBgImage;
	/** 再ログイン可能な企業リスト */
	public List<OptionItem> loginableCorporations;

	/** アラートメッセージ */
	public void addAlerts(String...alerts) {
		addAlerts(Arrays.asList(alerts));
	}

	/** アラートメッセージ */
	public void addAlerts(Collection<String> alerts) {
		if (this.alerts == null)
			this.alerts = new ArrayList<>();
		this.alerts.addAll(alerts);
	}

	/** 成功メッセージ */
	public void addSuccesses(String...successes) {
		addSuccesses(Arrays.asList(successes));
	}

	/** 成功メッセージ */
	public void addSuccesses(Collection<String> successes) {
		if (this.successes == null)
			this.successes = new ArrayList<>();
		this.successes.addAll(successes);
	}

	/** 警告メッセージ */
	public void addWarns(String... warns) {
		addWarns(Arrays.asList(warns));
	}

	/** 警告メッセージ */
	public void addWarns(Collection<String> warns) {
		if (this.warns == null)
			this.warns = new ArrayList<>();
		this.warns.addAll(warns);
	}
}
