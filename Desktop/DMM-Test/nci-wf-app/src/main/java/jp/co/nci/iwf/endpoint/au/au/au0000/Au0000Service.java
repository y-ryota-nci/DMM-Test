package jp.co.nci.iwf.endpoint.au.au.au0000;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.LoginResult;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * なりすまし先選択画面サービス
 *
 *
 */
@BizLogic
public class Au0000Service extends BaseService implements CodeBook {
	@Inject private CorporationService corp;
	@Inject private AccessLogService accesslog;
	@Inject private Logger log;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Au0000Response init(BaseRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		// 権限判定
		throwIfNoAuthority();

		// 初期選択内容
		final Au0000Response res = createResponse(Au0000Response.class, req);

		// システムプロパティ「管理者になりすましを許可」が有効か？
		if (corpProp.getBool(CorporationProperty.ALLOW_ADMIN_TO_IMPERSONATE, false)) {
			// 企業の選択肢
			res.corporations = corp.getMyCorporations(false);

			res.corporationCode = login.getCorporationCode();
			res.userCode = login.getUserCode();
			res.userName = login.getUserName();
			res.userAddedInfo = login.getUserAddedInfo();
			res.organizationCode = login.getOrganizationCode();
			res.organizationAddedInfo = login.getOrganizationAddedInfo();
			res.organizationTreeName = login.getOrganizationTreeName();
			res.success = true;

			// なりすまし先選択画面を表示したので、なりすまし要求をOFFにする
			login.setRequireSpoofing(false);
		}
		else {
			res.addAlerts(i18n.getText(MessageCd.MSG0167, MessageCd.impersonation));
			res.success = false;
		}
		return res;
	}

	/** なりすまし権限が無ければ例外スロー */
	private void throwIfNoAuthority() {
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (!login.isAspAdmin() && !login.isCorpAdmin() && !login.isGroupAdmin()) {
			throw new ForbiddenException("ASP管理者でも企業管理者でもグループ企業管理者でもないユーザは、なりすまし出来ません。");
		}
	}

	/**
	 * なりすましの実施
	 * @param req
	 * @return
	 */
	public BaseResponse spoof(Au0000Request req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final Map<String, String> logItems = new HashMap<>();

		final String error = validate(req);
		if (isEmpty(error)) {
			final LoginInfo current = sessionHolder.getLoginInfo();
			if (eq(req.corporationCode, current.getCorporationCode())
					&& eq(req.userAddedInfo, current.getUserAddedInfo())) {
				final String msg = "なりすまし前となりすまし先がと同一ユーザであったので、何もしませんでした。";
				log.info(msg);
				logItems.put("message", msg);
			}
			else {
				// なりすまし先ユーザのログイン者情報を生成し、自分のセッションを書き換える
				final AuthenticateService auth = AuthenticateService.get();
				final LoginInfo newLogin = auth.updateSessionBySpoofing(req.corporationCode, req.userAddedInfo);
				res.loginInfo = newLogin;

				final String msg = i18n.getText(MessageCd.MSG0164,
						newLogin.getCorporationName(), newLogin.getUserAddedInfo(), newLogin.getUserName());
				log.info(msg);
				res.addSuccesses(msg);
				logItems.put("message", msg);
			}
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
			accesslog.appendDetail(error);
			logItems.put("message", error);
		}

		// アクセスログも書く
		final Long accessLogId = accesslog.loadAccessLogId();
		if (accessLogId != null) {
			accesslog.appendDetail(logItems);
			if (!res.success)
				accesslog.updateResult(accessLogId, res.success);
		}

		return res;
	}

	/** なりすましのバリデーション */
	private String validate(Au0000Request req) {
		// 権限判定
		throwIfNoAuthority();

		// 企業コードが必須
		if (isEmpty(req.corporationCode))
			return i18n.getText(MessageCd.MSG0001, MessageCd.corporation);
		// ログインIDが必須
		if (isEmpty(req.userAddedInfo))
			return i18n.getText(MessageCd.MSG0001, MessageCd.userAddedInfo);

		// システムプロパティ「管理者になりすましを許可」が有効か？
		if (!corpProp.getBool(CorporationProperty.ALLOW_ADMIN_TO_IMPERSONATE, false))
			return i18n.getText(MessageCd.MSG0167, MessageCd.impersonation);

		// 保持している権限に応じた「なりすまし」可能な企業か
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (login.isAspAdmin()) {
			;	// ASP管理者はどんな企業に対しても「なりすまし」が出来る
		}
		else if (login.isGroupAdmin()) {
			// グループ管理者は自グループ内の企業にだけ「なりすまし」出来る
			final Optional<WfmCorporation> op = corp.getWfmCorporationByGroup(login.getCorporationGroupCode())
					.stream()
					.filter(c -> eq(c.getCorporationCode(), req.corporationCode))
					.findFirst();
			if (!op.isPresent())
				return i18n.getText(MessageCd.MSG0162);
		}
		else if (login.isCorpAdmin()) {
			// 企業管理者は自社内で「なりすまし」が出来る
			if (!eq(req.corporationCode, login.getCorporationCode()))
				return i18n.getText(MessageCd.MSG0161);
		}
		else {
			throw new ForbiddenException("なりすまし権限がありません");
		}

		// なりすまし先ユーザがログイン可能な状態にあること
		final AuthenticateService auth = AuthenticateService.get(AuthenticateService.DB);
		final LoginResult r = auth.tryLogin(req.corporationCode, req.userAddedInfo);
		if (!r.isSuccess())
			return auth.getLoginResultMessage(r);

		return null;
	}
}
