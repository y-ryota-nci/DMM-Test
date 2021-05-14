package jp.co.nci.iwf.component.authenticate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LockFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.custom.WfcUserPassword;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.component.i18n.TimeZoneService;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 認証サービスの基底クラス
 */
public abstract class AuthenticateServiceBase extends BaseService implements AuthenticateService {

	/** 初回アクセス時のURLを保存する際のキー */
	private static final String FIRST_ACCESS_URI = "__FIRST_ACCESS_URI__";

	@Inject private Logger log;
	@Inject protected WfInstanceWrapper wf;
	@Inject protected AuthenticateRepository repository;
	@Inject protected CorporationPropertyService propService;
	@Inject protected MailService mailService;
	@Inject protected TimeZoneService timeZoneService;
	@Inject protected AccessLogService accesslog;

	/**
	 * ログイン済みか
	 * @return
	 */
	public boolean isAuthenticated() {
		return sessionHolder.isAuthenticated();
	}

	/**
	 * IWF API用ユーザロールを生成(セッションへ格納はしない)
	 * @param loginInfo ログイン情報
	 * @return
	 */
	protected WfUserRole createWfUserRole(LoginInfo user) {
		final WfUserRole ur = new WfUserRoleImpl();
		ur.setCorporationCode(user.getCorporationCode());
		ur.setUserCode(user.getUserCode());
		ur.setAuthTransferList(new ArrayList<>());
		try {
			ur.setIpAddress(hsr.getRemoteAddr());
		}
		catch (Exception e) {
			ur.setIpAddress("0.0.0.0");
		}

		return ur;
	}

	/** パスワードマスタ取得 */
	protected WfmUserPassword getWfmUserPassowrd(String corporationCode, String userCode) {
		SearchWfmUserPasswordInParam in = new SearchWfmUserPasswordInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		in.setValidStartDate(today());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		List<WfcUserPassword> list = wf.searchWfmUserPassword(in).getUserPasswordList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/** 最終ログイン日時およびアカウントロック状態の更新 */
	@Transactional
	public boolean updatePassword(WfmUserPassword src, LoginResult result) {
		final WfmUserPassword entity = new WfcUserPassword();
		copyProperties(src, entity);

		if (result == LoginResult.Success || result == LoginResult.ChangePassword) {
			// 処理前の「ログイン時刻」を「前回ログイン時刻」へ書き写し
			final Timestamp loginTimestampPrev = src.getLoginTimestamp();
			entity.setLoginNgCount(0L);
			entity.setLoginTimestamp(new Timestamp(System.currentTimeMillis()));
			entity.setLoginTimestampPrev(loginTimestampPrev);
		}
		else {
			long loginNgCount = src.getLoginNgCount() == null ? 0 : src.getLoginNgCount().longValue();
			++loginNgCount;

			// ログイン連続失敗許容回数を超過すれば、アカウントロック状態にする
			Long maxLoginNgCount = propService.getLong(CorporationProperty.LOGIN_ALLOWED_FAILURE_COUNT);
			if (maxLoginNgCount != null && maxLoginNgCount > 0 && maxLoginNgCount < loginNgCount) {
				entity.setLockFlag(LockFlag.ON);
			}
			entity.setLoginNgCount(loginNgCount);
		}

		// パスワードマスタ更新
		final UpdateWfmUserPasswordInParam in = new UpdateWfmUserPasswordInParam();
		in.setWfmUserPassword(entity);
		in.setWfUserRole(toWfUserRole(src));
		final UpdateWfmUserPasswordOutParam out = wf.updateWfmUserPassword(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
			throw new InternalServerErrorException(out.getReturnMessage());
		}

		// 今回新たにアカウントロックされるのであれば、メール通知する
		return LockFlag.OFF.equals(src.getLockFlag()) && LockFlag.ON.equals(entity.getLockFlag());
	}

	/** アカウントロック通知メールの送信 */
	protected void sendAccountLockMail(WfvUserBelong user, WfmUserPassword userPassword) {
		// 置換文字列Map
		final Map<String, String> variables = new HashMap<>();
		variables.put(MailVariables.LOGIN_ID.toString(), user.getUserAddedInfo());
		variables.put(MailVariables.LOGIN_USER_NAME.toString(), user.getUserName());

		// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
		// 操作者の動作によりロックが発生したのだから、送信先は操作者ユーザ
		if (isNotEmpty(user.getMailAddress())) {
			final MailEntry entry = new MailEntry(
					user.getDefaultLocaleCode(), user.getMailAddress(), variables);
			final MailTemplate template = mailService.toTemplate(
					MailTemplateFileName.ACCOUNT_LOCK, user.getCorporationCode());
			mailService.send(template, entry);
		}
	}

	/** パスワードが期限切れか */
	protected boolean isOutOfDate(WfmUserPassword userPassword) {
		final Date today = today();
		final Date from = userPassword.getValidStartDate();
		if (from == null || from.compareTo(today) >= 0) {
			return false;
		}
		// パスワード有効期間（一定期間経過したらパスワード変更が必要）
		Integer span = propService.getInt(CorporationProperty.PASSWORD_VALIDITY_TERM);
		if (span == null || span <= 0) {
			return false;
		}
		final Date to = addDay(from, span - 1);
		return !between(today, from, to);
	}

	/** 所属／組織／役職の各マスタが期限切れか */
	protected boolean isOutOfDate(List<WfvUserBelongImpl> userBelongList) {
		for (WfvUserBelongImpl userBelong : userBelongList) {
			if (!isOutOfDate(userBelong)) {
				// 有効なのが１つでもあればよい
				return true;
			}
		}
		return false;
	}

	/** 所属／組織／役職の各マスタが期限切れか */
	protected boolean isOutOfDate(WfvUserBelong ub) {
		Date today = today();
		// ユーザ所属の有効期限
		Date from = ub.getValidStartDateUserBelong();
		Date to = ub.getValidEndDateUserBelong();
		if (!between(today, from, to)) {
			return false;
		}
		// 組織の有効期限
		from = ub.getValidStartDateOrganization();
		to = ub.getValidEndDateOrganization();
		if (!between(today, from, to)) {
			return false;
		}
		// 役職の有効期限
		from = ub.getValidStartDatePost();
		to = ub.getValidEndDatePost();
		if (!between(today, from, to)) {
			return false;
		}
		// ユーザの有効期限
		from = ub.getValidStartDateUser();
		to = ub.getValidEndDateUser();
		if (!between(today, from, to)) {
			return false;
		}
		return true;
	}

	/** ユーザマスタ抽出 */
	protected List<WfmUser> getWfmUser(String corporationCode, String userAddedInfo) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		return wf.searchWfmUser(in).getUserList();
	}

	/** ユーザ所属ビュー抽出 */
	protected List<WfvUserBelongImpl> getWfvUserBelongList(String corporationCode, String userAddedInfo) {
		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {new OrderBy(true, WfcUserBelong.JOB_TYPE)});
		return wf.searchWfvUserBelong(in).getUserBelongList();
	}

	/** ログイン済みユーザと同一ユーザか */
	protected boolean isSameCurrentUser(String corporationCode, String userAddedInfo) {
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		return loginInfo != null
				&& eq(corporationCode, loginInfo.getCorporationCode())
				&& eq(userAddedInfo, loginInfo.getUserAddedInfo());
	}

	/** WF API用のユーザロールを生成 */
	protected WfUserRole toWfUserRole(WfmUserPassword user) {
		final WfUserRole ur = new WfUserRoleImpl();
		ur.setCorporationCode(user.getCorporationCode());
		ur.setUserCode(user.getUserCode());
		ur.setAuthTransferList(new ArrayList<>());
		try {
			ur.setIpAddress(hsr.getRemoteAddr());
		}
		catch (Exception e) {
			ur.setIpAddress("0.0.0.0");
		}
		return ur;
	}

	/** 全企業を抽出 */
	protected List<WfmCorporation> getAllCorporations() {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		in.setOrderBy(new OrderBy[]{
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_GROUP_CODE),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_ADDED_INFO),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_CODE),
		});
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 組織を抽出 */
	protected WfcOrganization getWfmOrganization(String corporationCode, String organizationCode) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		final List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}
	/**
	 * ログイン者情報を生成(セッションへ格納はしない)
	 * @param newLocaleCode 言語
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 生成されたログイン者情報
	 */
	protected LoginInfo createLoginInfo(String newLocaleCode, String corporationCode, String userAddedInfo) {
		// ログイン者の使うべき言語コードを決定
		String localeCode = resolveLocaleCode(newLocaleCode, corporationCode, userAddedInfo);
		wf.setLocale(localeCode);

		// ユーザ情報
		final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(corporationCode, userAddedInfo);
		if (belongs == null || belongs.isEmpty())
			throw new InternalServerErrorException("ユーザ所属がありません");

		final WfvUserBelongImpl src = belongs.get(0);	// WFV_USER_BELONG.JOB_TYPEでソートされているので、主務が先頭
		final LoginInfo loginInfo = new LoginInfo();
		loginInfo.setCorporationCode(src.getCorporationCode());
		loginInfo.setCorporationName(src.getCorporationName());
		loginInfo.setCorporationGroupCode(src.getCorporationGroupCode());
		loginInfo.setUserCode(src.getUserCode());
		loginInfo.setUserName(src.getUserName());
		loginInfo.setUserAddedInfo(src.getUserAddedInfo());
//		loginInfo.setUserNameAbbr(src.getuserNameAbbr());
		loginInfo.setOrganizationCode(src.getOrganizationCode());
		loginInfo.setOrganizationName(src.getOrganizationName());
		loginInfo.setOrganizationTreeName(src.getOrganizationTreeName());
		loginInfo.setOrganizationAddedInfo(src.getOrganizationAddedInfo());
		loginInfo.setPostCode(src.getPostCode());
		loginInfo.setPostName(src.getPostName());
		loginInfo.setPostAddedInfo(src.getPostAddedInfo());
		loginInfo.setTelNum(src.getTelNum());
//		loginInfo.setTelNumCel(src.getTelNumCelUser());
		loginInfo.setMailAddress(src.getMailAddress());
		loginInfo.setExtendedInfo01(src.getExtendedInfo01());
		loginInfo.setSbmtrAddr(src.getSbmtrAddr());
		loginInfo.setOrganizationCodeUp3(src.getOrganizationCodeUp3());
		loginInfo.setOrganizationCode5(src.getOrganizationCode5());
		loginInfo.setOrganizationName5(src.getOrganizationName5());
		loginInfo.setPayApplCd(src.getPayApplCd());
		loginInfo.setTimeZone(timeZoneService.resolveTimeZone().toString());
		loginInfo.setLocaleCode(localeCode);
		loginInfo.setOrganizationCodes(belongs.stream()
				.map(b -> b.getOrganizationCode())
				.distinct()
				.sorted()
				.collect(Collectors.toList()));

		// このユーザの所有しているロールCD
		final Set<String> menuRoleCds = repository.getMenuRoleCds(belongs);
		loginInfo.setMenuRoleCodes(menuRoleCds);

		// このユーザの所有している参加者ロールCD
		final Set<String> assignRoleCds = repository.getAssignRoleCds(corporationCode, belongs);
		loginInfo.setAssignRoleCodes(assignRoleCds);

		// このユーザの所有している文書管理用の参加者ロールCDs
		final Set<String> docAssignRolesCds = repository.getDocAssignRoleCds(belongs);
		loginInfo.setDocAssignRoleCodes(docAssignRolesCds);

		// ロールCDでアクセス可能なメニュー
		// ⇒メニューから即新規起案するように定義されているなら、URLを新規起案リンクに置換
		final List<MwmMenu> accessibleMenus = getAccessibleMenus(
				src.getCorporationCode(), belongs, menuRoleCds, localeCode);
		loginInfo.setAccessibleMenus(accessibleMenus);

		// メニューからアクセス可能な画面IDを抽出
		final Set<String> accessibleScreenIds = repository.getAccessibleScreenIds(accessibleMenus);
		loginInfo.setAccessibleScreenIds(accessibleScreenIds);

		// このユーザのトップページ
		final String topPageUrl = loginInfo.getAccessibleMenus().stream()
				.map(menu -> removeBaseUrl(menu.getUrl()))
				.filter(url -> isNotEmpty(url) && !eq("#", url))
				.findFirst()
				.orElseThrow(() -> new InternalServerErrorException("このユーザにアクセス可能なメニューがありません"));
		loginInfo.setTopPageUrl(topPageUrl);

		// 主務組織
		final WfcOrganization org = getWfmOrganization(src.getCorporationCode(), src.getOrganizationCode());
		loginInfo.setMainOrganizationInfo(new OrganizationInfo(org));

		// なりすまし要求するか
		if (loginInfo.isAspAdmin() || loginInfo.isCorpAdmin() || loginInfo.isGroupAdmin()) {
			if (corpProp.getBool(CorporationProperty.ALLOW_ADMIN_TO_IMPERSONATE, false)) {
				loginInfo.setRequireSpoofing(true);
			}
		}

		// 再ログイン可能な企業リスト：ログインIDに紐付く各企業のユーザマスタ
		final List<WfmUser> users = getWfmUserByUserAddedInfo(loginInfo.getUserAddedInfo());
		final List<OptionItem> corporations = users.stream()
				.map(u -> new OptionItem(u.getCorporationCode(), u.getCorporationName()))
				.collect(Collectors.toList());
		loginInfo.setLoginableCorporations(corporations);

		return loginInfo;
	}

	/** 同一ログインIDをもつユーザマスタを抽出 */
	private List<WfmUser> getWfmUserByUserAddedInfo(String userAddedInfo) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		return wf.searchWfmUser(in).getUserList();
	}

	/** アクセス可能なメニューを抽出。必要なら新規起案用のリンクに置換 */
	protected List<MwmMenu> getAccessibleMenus(
			String corporationCode,
			List<WfvUserBelongImpl> belongs,
			Collection<String> menuRoleCds,
			String localeCode
	) {
		// メニューIDを置換するための画面プロセス／メニュー連携マスタを抽出
		final Map<Long, MwmScreenProcessMenu> spmMap = repository.getMwmScreenProcessMenu(corporationCode)
				.stream().collect(Collectors.toMap(spm -> spm.getMenuId(), spm -> spm));

		// URLが「-」で定義されていればそれは新規申請用のメニューである。
		// この場合、新規申請を「NA0010 新規申請」からではなくメニューからダイレクトに行うので、
		// URLを新規起案用のURLに置き換える
		final List<MwmMenu> menus = repository.getAccessibleMenus(belongs, menuRoleCds, localeCode);
		for (MwmMenu m : menus) {
			if (eq("-", m.getUrl())) {
				MwmScreenProcessMenu spm = spmMap.get(m.getMenuId());
				if (spm != null) {
					m.setUrl(String.format("../vd/vd0310.html?screenProcessId=%d&trayType=%s",
							spm.getScreenProcessId(), TrayType.NEW.toString()));
				}
			}
		}
		return menus;
	}

	private static final String BASE_URL = "../";

	/** 相対パスから '../'を除去 */
	private String removeBaseUrl(String url) {
		if (url.startsWith(BASE_URL))
			return url.substring(BASE_URL.length());
		return url;
	}

	/**
	 * ログイン者の使うべき言語コードを決定して返す
	 * @param newLocaleCode 変更後の新言語コード（省略可）
	 * @param corporationCode 企業コード
	 * @param userAddedInfo ユーザ付加情報（ログインID）
	 * @return
	 */
	protected String resolveLocaleCode(String newLocaleCode, String corporationCode, String userAddedInfo) {
		// 言語が指定されていればその言語を使用
		String localeCode = newLocaleCode;

		// 既存のログイン者情報（＝セッション）に言語設定されていれば、その言語を使用
		if (isEmpty(localeCode)) {
			if (sessionHolder != null) {
				final LoginInfo loginInfo = sessionHolder.getLoginInfo();
				if (loginInfo != null && isNotEmpty(loginInfo.getLocaleCode()))
					localeCode = loginInfo.getLocaleCode();
			}
		}
		// ユーザマスタに言語指定があればそれを使用
		if (isEmpty(localeCode)) {
			List<WfmUser> users = getWfmUser(corporationCode, userAddedInfo);
			if (!users.isEmpty()) {
				localeCode = users.get(0).getDefaultLocaleCode();
			}
		}
		// ブラウザの言語設定があれば、その言語を使用
		if (isEmpty(localeCode)) {
			if (hsr != null && hsr.getLocale() != null)
				localeCode = hsr.getLocale().getLanguage();
		}
		// いずれもなければシステム標準言語を使用
		if (isEmpty(localeCode)) {
			localeCode = LocaleService.DEFAULT_LOCALE.getLanguage();
		}
		return localeCode;
	}

	/**
	 * 「ログイン前に初回リクエストされたURI」を返す。
	 * @return
	 */
	public String getFirstRequestURL() {
		if (sessionHolder != null && sessionHolder.getFlushScope() != null)
			return sessionHolder.getFlushScope().get(FIRST_ACCESS_URI);
		return null;
	}

	/**
	 * 現在のリクエストURIを「ログイン前に初回リクエストされたURI」として保存
	 */
	public void saveFirstRequestURL(HttpServletRequest req) {
		final String path = MiscUtils.getRelativePageURL(req);
		saveFirstRequestURL(path);
	}

	/**
	 * 現在のリクエストURIを「ログイン前に初回リクエストされたURI」として保存
	 * （Endpointに対するリクエストなので、refererから元のURIを求める）
	 */
	public void saveFirstRequestURL(ContainerRequestContext crc) {
		// Endpointに対するリクエストなので、refererから元のHTMLのURIを求める
		final MultivaluedMap<String, String> headers = crc.getHeaders();
		final String referer = headers.getFirst("referer");
		final String ROOT = "/page/";
		final String path = referer.substring(referer.indexOf(ROOT) + ROOT.length());
		if (path.endsWith(".html"))
			saveFirstRequestURL(path);
	}

	/** URIを「ログイン前に初回リクエストされたURI」として保存 */
	private void saveFirstRequestURL(String path) {
		try {
			if (sessionHolder != null && sessionHolder.getFlushScope() != null)
				sessionHolder.getFlushScope().putIfAbsent(FIRST_ACCESS_URI, path);
		}
		catch (IllegalArgumentException e) {
			// 要求URLが/page/以下でないので、初回要求URLとしては保存できないが
			// エラーとして続行できないわけではないので、握りつぶす
			log.warn(e.getMessage());
		}
	}

	/** ログイン画面を使うか */
	@Override
	public boolean isUseLoginScreen() {
		return propService.getBool(CorporationProperty.USE_LOGIN_SCREEN, true);
	}

	/** シングルサインオン（SSO)認証を使うか */
	@Override
	public boolean isEnableSSO() {
		return false;
	}

	/**
	 * ログイン処理結果に応じたメッセージを返す
	 * @param result ログイン処理結果
	 * @return
	 */
	public String getLoginResultMessage(LoginResult result) {
		if (LoginResult.isSuccess(result))
			return "";
		switch (result) {
		case InvalidPassword:
		case NotExistUser:
		case InvalidUserAddedInfo:
		case NotAuthenticatedLDAP:
			return i18n.getText(MessageCd.MSG0035);
		case PasswordOutOfDate:
			return i18n.getText(MessageCd.MSG0042);
		case LockedOut:
			return i18n.getText(MessageCd.MSG0038);
		case DupulicatedUserAddedInfo:
			return i18n.getText(MessageCd.MSG0036);
		case InvalidUserBelong:
			return i18n.getText(MessageCd.MSG0037);
		case NoAccessibleMenu:
			return i18n.getText(MessageCd.MSG0039);
		case NoMenuRoleCd:
			return i18n.getText(MessageCd.MSG0040);
		case LoginForAdminOnly:
			return i18n.getText(MessageCd.MSG0157);
		case NotAuthenticatedIWA:
			return i18n.getText(MessageCd.MSG0166);
		default:
			log.info("loginResult={}", result);
			return i18n.getText(MessageCd.MSG0043);
		}
	}


	/** アクセスログ明細に記録するためのエントリをMap形式で返す */
	public Map<String, String> toAccessLogDetailMap(LoginResult result, LoginInfo login) {
		final Map<String, String> logItems = new HashMap<>();
		logItems.put("authMethod", toString());
		logItems.put("loginResult", result.toString());

		logItems.put("message", getLoginResultMessage(result));
		if (LoginResult.isSuccess(result) && login != null){
			logItems.put("logged in as", login.getCorporationCode() + "/" + login.getUserAddedInfo());
		}
		return logItems;
	}

	@Override
	public String toString() {
		String name = getClass().getSimpleName();
		int pos = name.indexOf("$");
		return pos < 0 ? name : name.substring(0, pos);
	}


	/**
	 * ログイン可能かを判定 【パスワード判定無し】
	 * @param corporationCode 企業コード（省略可）
	 * @param userAddedInfo ログインID
	 * @return 認証結果
	 */
	public LoginResult tryLogin(String corporationCode, String userAddedInfo) {
		if (isEmpty(userAddedInfo)) {
			return LoginResult.InvalidUserAddedInfo;
		}
		else if (isAuthenticated() && isSameCurrentUser(corporationCode, userAddedInfo)) {
			// すでにログイン済み、かつログインユーザがリクエストされたログインIDと同じならログイン済み扱い
			return LoginResult.AlreadyLoggedIn;
		}

		LoginResult result = LoginResult.Success;
		final List<WfmUser> users = getWfmUser(corporationCode, userAddedInfo);
		if (users == null || users.isEmpty()) {
			result = LoginResult.NotExistUser;
		}
		else if (users.size() > 1){
			// ユニークなはずなのに、同一USER_ADDED_INFOをもつユーザが複数存在する
			result = LoginResult.DupulicatedUserAddedInfo;
		}
		else {
			// 所属／役職／組織のチェック
			final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(corporationCode, userAddedInfo);

			if (belongs == null || belongs.isEmpty()) {
				// どこにも所属していない
				result = LoginResult.InvalidUserBelong;
			}
			else if (isOutOfDate(belongs)) {
				// 所属／役職／組織の有効期限切れ
//				result = LoginResult.UserBelongOutOfDate;
				result = LoginResult.InvalidUserBelong;
			}
			else {
				// メニューロール
				final Set<String> menuRoleCds = repository.getMenuRoleCds(belongs);
				final List<MwmMenu> accessibleMenus =
					repository.getAccessibleMenus(belongs, menuRoleCds, localeService.getLocaleCode());
				final Boolean isMaintenanceMode = corpProp.getBool(CorporationProperty.LOGIN_FOR_ADMIN_ONLY);
				if (menuRoleCds.isEmpty()) {
					// メニューロールを１つも所有していない
					result = LoginResult.NoMenuRoleCd;
				}
				else if (accessibleMenus == null || accessibleMenus.isEmpty()) {
					// アクセス可能なメニュー０件
					result = LoginResult.NoAccessibleMenu;
				}
				else if (isMaintenanceMode != null
						&& isMaintenanceMode.booleanValue()
						&& !in(menuRoleCds, MenuRoleCodes.ASP, MenuRoleCodes.CorpAdmin)) {
					// 管理者以外ログイン不可なのに、通常ユーザがログインしてきた
					result = LoginResult.LoginForAdminOnly;
				}
			}
		}
		return result;
	}

	/**
	 * ログイン認証結果をもとに、セッション情報を書き換え
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @param corporationCode 企業コード
	 * @param userAddedInfo ログインID
	 * @return
	 */
	@Override
	public LoginInfo updateSessionIfSuccess(LoginResult result, String newLocaleCode, String corporationCode, String userAddedInfo) {
		LoginInfo loginInfo = null;
		String msg = null;
		if (result.isSuccess()) {
			// ログイン情報を生成し、セッションへ格納
			loginInfo = createLoginInfo(newLocaleCode, corporationCode, userAddedInfo);
			sessionHolder.setLoginInfo(loginInfo);

			final WfUserRole wfUserRole = createWfUserRole(loginInfo);
			sessionHolder.setWfUserRole(wfUserRole);

			msg = String.format("=== logged in as '%s/%s' ===",
					loginInfo.getCorporationCode(), loginInfo.getUserAddedInfo());

			// セッション固定攻撃対策として、ログイン成功時にセッションIDを変更する
			hsr.changeSessionId();
		}
		else {
			// エラーあり
			msg = getLoginResultMessage(result);
		}
		log.info(msg);

		// アクセスログにも書く
		final Long accessLogId = accesslog.loadAccessLogId();
		if (accessLogId != null) {
			accesslog.updateResult(accessLogId, result.isSuccess());	// AccessLogFilterより先に処理結果を書き込むことで、結果を確定させてしまう
			accesslog.appendDetail(accessLogId, toAccessLogDetailMap(result, loginInfo));
		}
		return loginInfo;
	}

	/**
	 * なりすまし先ユーザ情報でセッション情報を書き換え
	 * @param corporationCode 企業コード
	 * @param userAddedInfo ログインID
	 * @return 新しいログイン情報
	 */
	public LoginInfo updateSessionBySpoofing(String corporationCode, String userAddedInfo) {
		final LoginInfo old = sessionHolder.getLoginInfo();
		final LoginInfo newLogin = createLoginInfo(old.getLocaleCode(), corporationCode, userAddedInfo);

		// なりすまし元のユーザ情報を記録しておく（複数回のなりすましでも、原初のユーザ情報を維持する）
		newLogin.setSpoofingCorporationCode(defaults(old.getSpoofingCorporationCode(), old.getCorporationCode()));
		newLogin.setSpoofingUserCode(defaults(old.getSpoofingUserCode(), old.getUserCode()));
		newLogin.setSpoofingUserAddedInfo(defaults(old.getSpoofingUserAddedInfo(), old.getUserAddedInfo()));
		// すでになりすましているので、これ以上のなりすましは不要
		newLogin.setRequireSpoofing(false);
		sessionHolder.setLoginInfo(newLogin);

		final WfUserRole wfUserRole = createWfUserRole(newLogin);
		sessionHolder.setWfUserRole(wfUserRole);

		final String msg = i18n.getText(MessageCd.MSG0164,
				newLogin.getCorporationName(), newLogin.getUserAddedInfo(), newLogin.getUserName());
		log.info(msg);

		return newLogin;
	}

	/**
	 * ログイン認証結果をもとに、セッション情報を書き換え【SSO認証トークンをHTTPリクエストから吸上げて認証】
	 * @param result ログイン認証結果
	 * @param newLocaleCode 新しい言語コード
	 * @return
	 */
	@Override
	public LoginInfo updateSessionIfSuccess(LoginResult result, String newLocaleCode) {
		throw new IllegalAccessError("正しくないメソッド呼び出しです。");	// AD/LDAP/DB認証では updateSessionIfSuccess(LoginResult, String, String, String)を使うこと
	}
}
