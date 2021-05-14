package jp.co.nci.iwf.endpoint.unsecure.login;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.LoginResult;
import jp.co.nci.iwf.component.system.AnnouncementService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * ログイン画面サービス
 */
@BizLogic
public class LoginService extends BaseService implements CodeBook {
	@Inject private Logger log;
	@Inject private AnnouncementService announce;
	@Inject private MwmLookupService lookup;
	@Inject private AccessLogService accessLog;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public LoginInitResponse init(BaseRequest req) {
		final LoginInitResponse res = createResponse(LoginInitResponse.class, req);

		// お知らせ情報
		res.setAnnouncements(announce.getLoginAnnouncement(5));
		res.success = true;

		return res;
	}

	/**
	 * ログイン認証を行う
	 * @param req
	 * @return
	 */
	@Transactional
	public LoginResponse login(LoginRequest req) {
		final String corporationCode = req.getCorporationCode();
		final String userAddedInfo = req.getUserAddedInfo();
		final String password = req.getPassword();
		final LoginResponse res = createResponse(LoginResponse.class, req);

		// ログイン認証判定
		final AuthenticateService auth = AuthenticateService.get();
		final LoginResult result = auth.tryLogin(corporationCode, userAddedInfo, password);

		log.warn("loginResult={}", result);

		// ログイン認証が成功ならセッションを書き換え、失敗ならアクセスログを記録
		final String newLocaleCode = null;
		LoginInfo loginInfo = auth.updateSessionIfSuccess(result, newLocaleCode, corporationCode, userAddedInfo);

		if (result.isSuccess()) {
			res.loginInfo = loginInfo;
			res.redirectUrl = loginInfo != null ? loginInfo.getTopPageUrl() : null;
			res.success = loginInfo != null;

			HttpSession session = hsr.getSession(false);

			session.removeAttribute("CSS_CHANGE_FLAG");

			// MWM_LOOKUPからCSS切替対象となる会社コードを取得し、ログインユーザの会社コードと比較する。
			List<MwmLookup> mwmTargetCompanyLookupList = lookup.get(loginInfo.getLocaleCode(), corporationCode, LookupGroupId.CSS_TARGET_COMPANY_CODE);

			if (mwmTargetCompanyLookupList.isEmpty()){
				mwmTargetCompanyLookupList = lookup.get(loginInfo.getLocaleCode(), "ASP", LookupGroupId.CSS_TARGET_COMPANY_CODE);
			}

			List<String> targetCompanyCodeList = new ArrayList<String>();
			for (MwmLookup mwmTargetCompanyLookupData : mwmTargetCompanyLookupList){
				// 会社コードのみ抜出、リストに格納する。
				targetCompanyCodeList.add(mwmTargetCompanyLookupData.getLookupName());
			}

			// 切替フラグをセッションに保持する。
			session.setAttribute("TARGET_LIST", targetCompanyCodeList);
		}
		else {
			// エラーあり
			res.addAlerts(auth.getLoginResultMessage(result));
			res.success = false;
		}

		return res;
	}

	/**
	 * ログアウト処理を行う
	 * @return
	 */
	@Transactional
	public BaseResponse logout() {
		final HttpSession session = hsr.getSession(false);
		if (session != null) {
			log.info("=== loggged out ===");
			session.invalidate();
		}
		BaseResponse res = createResponse(BaseResponse.class, null, AppURL.LOGIN);
		res.success = true;
		return res;
	}

	/**
	 * 言語の変更
	 * @param newLocaleCode
	 * @return
	 */
	public BaseResponse changeLocaleCode(String newLocaleCode) {
		final BaseResponse res = createResponse(BaseResponse.class, null, null);

		// 選択可能な言語？
		if (localeService.isSelectableLocaleCode(newLocaleCode)) {
			// 現在のログイン情報のまま、セッション情報を新しい言語で書き換え
			final LoginInfo old = sessionHolder.getLoginInfo();
			final AuthenticateService auth = AuthenticateService.get();
			final LoginInfo newLoginInfo = auth.updateSessionIfSuccess(
					LoginResult.AlreadyLoggedIn, newLocaleCode, old.getCorporationCode(), old.getUserAddedInfo());

			// ログインした時点で「なりすまし」をする／しないはユーザが選択済みなので、聞き直すのはしつこいだろう。
			newLoginInfo.setRequireSpoofing(old.isRequireSpoofing());
			res.loginInfo = newLoginInfo;
			res.success = true;
			// 言語が変更されたので、ゼロから画面を読み直す
			res.redirectUrl = newLoginInfo.getTopPageUrl();

			// セッション内のログイン者情報も置換
			sessionHolder.setLoginInfo(newLoginInfo);
			res.success = true;
		}
		else {
			throw new BadRequestException("サポートされていない言語です -> " + newLocaleCode);
		}
		return res;
	}

	/**
	 *  （DMM専用）企業の切り替え
	 * @param newCorporationCode
	 * @return
	 */
	public BaseResponse changeCorporation(LoginChangeCorporationRequest req) {
		if (isEmpty(req.newCorporationCode))
			throw new BadRequestException("新企業コードが未指定です");

		// そもそも認証されていないとダメ（ここのEndpointは認証されなくても呼び出せる）
		final AuthenticateService auth = AuthenticateService.get();
		if (!auth.isAuthenticated())
			throw new ClientErrorException(Status.UNAUTHORIZED);

		final BaseResponse res = createResponse(BaseResponse.class, req);

		// 新しい企業コードでログイン可能か？
		final String userAddedInfo = sessionHolder.getLoginInfo().getUserAddedInfo();
		final LoginResult r = auth.tryLogin(req.newCorporationCode, userAddedInfo);
		if (!r.isSuccess()) {
			res.addAlerts(auth.getLoginResultMessage(r));
			res.success = false;
		}
		else {
			// 処理前のログイン者情報
			final LoginInfo old = sessionHolder.getLoginInfo();

			// 新しい企業コードでログイン
			final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			final LoginInfo newLogin = auth.updateSessionIfSuccess(r, localeCode, req.newCorporationCode, userAddedInfo);
			res.loginInfo = newLogin;
			res.loginableCorporations = newLogin.getLoginableCorporations();
			res.redirectUrl = newLogin.getTopPageUrl();
			res.success = true;

			// アクセスログにも記録
			accessLog.appendDetail(String.format("ユーザ [%s/%s] は [%s] から [%s] へログイン先企業を切り替えました。",
					old.getUserAddedInfo(),
					old.getUserName(),
					old.getCorporationName(),
					newLogin.getCorporationName()));
		}
		return res;
	}
}
