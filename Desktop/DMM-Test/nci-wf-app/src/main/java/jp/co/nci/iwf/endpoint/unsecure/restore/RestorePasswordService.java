package jp.co.nci.iwf.endpoint.unsecure.restore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserByHisLocaleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * パスワード復旧画面サービス
 */
@BizLogic
public class RestorePasswordService extends BaseService {
	@Inject private MailService mailService;
	@Inject private WfInstanceWrapper wf;
	@Inject private RestorePasswordRepository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 仮パスワード発行
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse send(RestorePasswordRequest req) {
		if (isEmpty(req.loginIdOrMailAddress))
			throw new BadRequestException("ログインIDまたはメールアドレスが未指定です");

		// 仮パスワードを生成し、それをメールに記載して送信する。
		// ただし、画面でメールアドレス入れるだけで他人のパスワードリセットし放題はヤバイので、
		// 実際にパスワードリセットが行われるのはメールに記載されているリンクを押下したときである。
		final BaseResponse res = createResponse(BaseResponse.class, req);

		// 宛先ユーザの特定
		final List<WfmUser> users = getUser(req);
		if (users.isEmpty()) {
			// ログインIDまたはメールアドレスに該当するユーザがいませんでした。管理者へご連絡ください。
			res.addAlerts(i18n.getText(MessageCd.MSG0175));
			res.success = false;
		}
		else if (users.size() > 1) {
			// ログインIDまたはメールアドレスに該当するユーザが複数いて特定できませんでした。管理者へご連絡ください。
			res.addAlerts(i18n.getText(MessageCd.MSG0176));
			res.success = false;
		}
		else if (isEmpty(users.get(0).getMailAddress())) {
			// あなたのアカウントにメールアドレスが設定されていないため、仮パスワードの発行が出来ません。管理者へご連絡ください。
			res.addAlerts(i18n.getText(MessageCd.MSG0178));
			res.success = false;
		}
		else {
			// 対象ユーザをそのデフォルト言語コードで引き直して、仮パスワード発行通知メールを送信
			final WfmUser user = getUserByHisLocale(users.get(0));
			sendOneTimePasswordMail(user);

			// あなたのメールアドレスに仮パスワード発行のお知らせを送信しましたので、ご確認ください（仮パスワードを活性化するまで、既存パスワードは依然として有効です）。
			res.addSuccesses(i18n.getText(MessageCd.MSG0177));
			res.success = true;
		}
		return res;
	}

	/** 仮パスワード発行通知メールを送信 */
	private void sendOneTimePasswordMail(final WfmUser user) {
		// 仮パスワード生成
		final String newPassword = SecurityUtils.randomPincode(6);	// PINコード＝数値のみ

		// パスワードリセットエントリの登録し、そのIDを暗号化
		final long id = repository.insertMwtResetPassword(user, newPassword);
		final String cipher = SecurityUtils.encrypt(String.valueOf(id));

		// 置換文字列Map
		final Map<String, String> variables = new HashMap<>();
		variables.put(MailVariables.LOGIN_ID, user.getUserAddedInfo());
		variables.put(MailVariables.LOGIN_USER_NAME, user.getUserName());
		variables.put(MailVariables.TEMPORARY_PASSWORD, newPassword);
		variables.put(MailVariables.CIPHER, cipher);
		variables.put(MailVariables.HOURS, String.valueOf(24));

		// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
		final MailEntry entry = new MailEntry(
				user.getDefaultLocaleCode(), user.getMailAddress(), variables);
		final MailTemplate template = mailService.toTemplate(
				MailTemplateFileName.ONETIME_PASSWORD, CorporationCode.ASP);
		mailService.send(template, entry);
	}

	private List<WfmUser> getUser(RestorePasswordRequest req) {
		final String userAddedInfo = req.loginIdOrMailAddress;
		List<WfmUser> users = getUserByLoginId(req.corporationCode, userAddedInfo);
		if (users.isEmpty()) {
			// ログインIDで該当なし なら、メールアドレスで探し直す
			users = getUserByMailAddress(req.corporationCode, req.loginIdOrMailAddress);
		}
		return users;
	}

	/** 対象ユーザのデフォルト言語コードでユーザマスタを抽出 */
	private WfmUser getUserByHisLocale(WfmUser src) {
		// 普通にやると操作者の言語コードになってしまうが、ここで必要なのは送信対象者の言語コードから。
		final SearchWfmUserByHisLocaleInParam in = new SearchWfmUserByHisLocaleInParam();
		in.setCorporationCode(src.getCorporationCode());
		in.setUserCode(src.getUserCode());
		return wf.searchWfmUserByHisLocale(in).getUserList().get(0);
	}

	/** ログインIDをベースにユーザ検索 */
	private List<WfmUser> getUserByLoginId(String corporationCode, String userAddedInfo) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		return wf.searchWfmUser(in).getUserList();
	}

	/** メールアドレスをベースにユーザ検索 */
	private List<WfmUser> getUserByMailAddress(String corporationCode, String mailAddress) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setMailAddress(mailAddress);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		return wf.searchWfmUser(in).getUserList();
	}
}
