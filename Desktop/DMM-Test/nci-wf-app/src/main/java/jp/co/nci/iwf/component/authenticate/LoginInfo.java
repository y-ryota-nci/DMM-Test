package jp.co.nci.iwf.component.authenticate;

import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.CDI;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.iwf.component.CodeBook.AppURL;
import jp.co.nci.iwf.component.CodeBook.MenuRoleCodes;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * ログイン者情報
 */
public class LoginInfo extends UserInfo {
	/** 企業グループコード */
	private String corporationGroupCode;
	/** 組織コードリスト(主務＋兼務) */
	private List<String> organizationCodes;
	/** 組織階層名称(主務) */
	private String organizationTreeName;
	/** 言語コード */
	private String localeCode;
	/** 所有しているメニューロールコード */
	private Set<String> menuRoleCodes;
	/** 所有している参加者ロール */
	private Set<String> assignRoleCodes;
	/** 所有している文書管理用の参加者ロールコード */
	private Set<String> docAssignRoleCodes;
	/** デフォルト言語コード */
	private String defaultLocaleCode;
	/** パスワード変更が必要か */
	private boolean changePassword;
	/** アクセス可能なメニュー */
	@JsonIgnore	/* JSON化の対象外 */
	private List<MwmMenu> accessibleMenus;
	/** アクセス可能な画面ID */
	@JsonIgnore	/* JSON化の対象外 */
	private Set<String> accessibleScreenIds;
	/** タイムゾーン */
	@JsonIgnore	/* JSON化の対象外 */
	private String timeZone;
	/** ログイン者主務組織情報 */
	@JsonIgnore	/* JSON化の対象外 */
	private OrganizationInfo mainOrganizationInfo;
	/** 本日のお知らせを表示済みか */
	private boolean displayAnnouncement;
	/** なりすまし元の企業コード */
	@JsonIgnore	/* JSON化の対象外 */
	private String spoofingCorporationCode;
	/** なりすまし元のユーザコード */
	@JsonIgnore	/* JSON化の対象外 */
	private String spoofingUserCode;
	/** なりすまし元のユーザ付加情報 */
	@JsonIgnore	/* JSON化の対象外 */
	private String spoofingUserAddedInfo;
	/** なりすまし画面への遷移が必要か */
	@JsonIgnore	/* JSON化の対象外 */
	private boolean requireSpoofing;
	/** このユーザのトップページURL */
	private String topPageUrl = AppURL.LOGIN;	// ログイン前はこれ。ログイン時に権限に合わせたトップページに書き換える
	/** ログイン可能な企業リスト */
	private List<OptionItem> loginableCorporations;

	/**
	 * スレッドに紐付くログイン者情報を返す
	 * @return
	 */
	public static LoginInfo get() {
		final SessionHolder sh = CDI.current().select(SessionHolder.class).get();
		return sh == null ? null : sh.getLoginInfo();
	}



	/** ASP管理者か */
	public boolean isAspAdmin() {
		return menuRoleCodes != null
				&& menuRoleCodes.contains(MenuRoleCodes.ASP);
	}

	/** グループ管理者か */
	public boolean isGroupAdmin() {
		return menuRoleCodes != null
				&& menuRoleCodes.contains(MenuRoleCodes.GroupAdmin);
	}

	/** 企業管理者か */
	public boolean isCorpAdmin() {
		return menuRoleCodes != null
				&& menuRoleCodes.contains(MenuRoleCodes.CorpAdmin);
	}

	/** ユーザ管理者か */
	public boolean isUserAdmin() {
		return menuRoleCodes != null
				&& menuRoleCodes.contains(MenuRoleCodes.UserAdmin);
	}

	/** 企業グループコード */
	public String getCorporationGroupCode() {
		return corporationGroupCode;
	}
	/** 企業グループコード */
	public void setCorporationGroupCode(String corporationGroupCode) {
		this.corporationGroupCode = corporationGroupCode;
	}

	/** 組織コードリスト(主務＋兼務) */
	public List<String> getOrganizationCodes() {
		return organizationCodes;
	}
	/** 組織コードリスト(主務＋兼務) */
	public void setOrganizationCodes(List<String> organizationCodes) {
		this.organizationCodes = organizationCodes;
	}

	/** 組織階層名称(主務) */
	public String getOrganizationTreeName() {
		return organizationTreeName;
	}
	/** 組織階層名称(主務) */
	public void setOrganizationTreeName(String organizationTreeName) {
		this.organizationTreeName = organizationTreeName;
	}

	/** 所有しているメニューロールコード */
	public Set<String> getMenuRoleCodes() {
		return menuRoleCodes;
	}
	/** 所有しているメニューロールコード */
	public void setMenuRoleCodes(Set<String> menuRoleCds) {
		this.menuRoleCodes = menuRoleCds;
	}

	/** 所有している参加者ロール */
	public Set<String> getAssignRoleCodes() {
		return assignRoleCodes;
	}
	/** 所有している参加者ロール */
	public void setAssignRoleCodes(Set<String> assignRoleCodes) {
		this.assignRoleCodes = assignRoleCodes;
	}

	/** 所有している文書管理用の参加者ロールコード */
	public Set<String> getDocAssignRoleCodes() {
		return docAssignRoleCodes;
	}
	/** 所有している文書管理用の参加者ロールコード */
	public void setDocAssignRoleCodes(Set<String> docAssignRoleCodes) {
		this.docAssignRoleCodes = docAssignRoleCodes;
	}

	/** 言語コード */
	public String getLocaleCode() {
		return localeCode;
	}
	/** 言語コード */
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	/** タイムゾーン */
	public String getTimeZone() {
		return timeZone;
	}
	/** タイムゾーン */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/** アクセス可能なメニュー */
	public List<MwmMenu> getAccessibleMenus() {
		return accessibleMenus;
	}
	/** アクセス可能なメニュー */
	public void setAccessibleMenus(List<MwmMenu> accessibleMenus) {
		this.accessibleMenus = accessibleMenus;
	}

	/** アクセス可能な画面ID */
	public Set<String> getAccessibleScreenIds() {
		return accessibleScreenIds;
	}
	/** アクセス可能な画面ID */
	public void setAccessibleScreenIds(Set<String> accessibleScreenIds) {
		this.accessibleScreenIds = accessibleScreenIds;
	}

	/** ログイン者主務組織情報 */
	public OrganizationInfo getMainOrganizationInfo() {
		return mainOrganizationInfo;
	}
	/** ログイン者主務組織情報 */
	public void setMainOrganizationInfo(OrganizationInfo mainOrganizationInfo) {
		this.mainOrganizationInfo = mainOrganizationInfo;
	}

	/** デフォルト言語コード */
	public String getDefaultLocaleCode() {
		return defaultLocaleCode;
	}
	/** デフォルト言語コード */
	public void setDefaultLocaleCode(String defaultLocaleCode) {
		this.defaultLocaleCode = defaultLocaleCode;
	}

	/** パスワード変更が必要か */
	public boolean isChangePassword() {
		return changePassword;
	}
	/** パスワード変更が必要か */
	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	/** 本日のお知らせを表示済みか */
	public boolean isDisplayAnnouncement() {
		return displayAnnouncement;
	}
	/** 本日のお知らせを表示済みか */
	public void setDisplayAnnouncement(boolean displayAnnouncementOfTheDay) {
		this.displayAnnouncement = displayAnnouncementOfTheDay;
	}

	/** なりすまし元の企業コード */
	public String getSpoofingCorporationCode() {
		return spoofingCorporationCode;
	}
	/** なりすまし元の企業コード */
	public void setSpoofingCorporationCode(String spoofingCorporationCode) {
		this.spoofingCorporationCode = spoofingCorporationCode;
	}

	/** なりすまし元のユーザコード */
	public String getSpoofingUserCode() {
		return spoofingUserCode;
	}
	/** なりすまし元のユーザコード */
	public void setSpoofingUserCode(String spoofingUserCode) {
		this.spoofingUserCode = spoofingUserCode;
	}

	/** なりすまし元のユーザ付加情報 */
	public String getSpoofingUserAddedInfo() {
		return spoofingUserAddedInfo;
	}
	/** なりすまし元のユーザ付加情報 */
	public void setSpoofingUserAddedInfo(String spoofingUserAddedInfo) {
		this.spoofingUserAddedInfo = spoofingUserAddedInfo;
	}

	/** なりすまし画面への遷移が必要か */
	public boolean isRequireSpoofing() {
		return requireSpoofing;
	}
	/** なりすまし画面への遷移が必要か */
	public void setRequireSpoofing(boolean requireSpoofing) {
		this.requireSpoofing = requireSpoofing;
	}

	/** このユーザのトップページURL */
	public String getTopPageUrl() {
		return topPageUrl;
	}
	/** このユーザのトップページURL */
	public void setTopPageUrl(String topPageUrl) {
		this.topPageUrl = topPageUrl;
	}

	/** ログイン可能な企業リスト */
	public List<OptionItem> getLoginableCorporations() {
		return loginableCorporations;
	}
	/** ログイン可能な企業リスト */
	public void setLoginableCorporations(List<OptionItem> loginableCorporations) {
		this.loginableCorporations = loginableCorporations;
	}
}
