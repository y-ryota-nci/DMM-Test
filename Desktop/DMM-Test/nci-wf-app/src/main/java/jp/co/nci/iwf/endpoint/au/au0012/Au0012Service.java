package jp.co.nci.iwf.endpoint.au.au0012;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.GetWfmUserPasswordListInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LockFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserPasswordImpl;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Repository;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Request;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Response;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * パスワード変更画面(管理者)サービス
 */
@BizLogic
@Typed(Au0012Service.class)
public class Au0012Service extends Au0011Service {
	@Inject
	private Logger log;
	@Inject
	private Au0011Repository repository;
	@Inject
	private MailService mail;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Override
	public Au0011Response init(Au0011Request req) {
		// 管理者用画面は対象ユーザを特定する必要がある
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.userCode))
			throw new BadRequestException("ユーザコードが未指定です");

		final Au0011Response res = createResponse(Au0011Response.class, req);

		// ユーザ情報＋最新のパスワード情報
		loadPasswordInfo(req, res);

		res.success = true;
		return res;
	}

	/** 通常のパスワード画面専用のバリデーション */
	@Override
	protected List<String> validateAu0011Only(Au0011Request req, WfmUserPassword pswd) {
		// パスワード変更画面(管理者用)なので、バリデーションはしない
		return new ArrayList<>();
	}

	/**
	 * パスワード変更要求（次回ログイン時に変更を強制）
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse changePasswordRequest(Au0011Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("ユーザコードが未指定です");

		final WfmUserPassword pswd = getWfmUserPassword(req);
		if (pswd == null)
			throw new NotFoundException("パスワードが存在しません");

		pswd.setChangeRequestFlag(CommonFlag.ON);
		UpdateWfmUserPasswordInParam in = new UpdateWfmUserPasswordInParam();
		in.setWfmUserPassword(pswd);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmUserPasswordOutParam out = wf.updateWfmUserPassword(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
			throw new WfException(out);
		}

		final Au0011Response res = createResponse(Au0011Response.class, req);
		loadPasswordInfo(req, res);
		res.addSuccesses(i18n.getText(MessageCd.MSG0235));
		res.success = true;
		return res;
	}

	/** ユーザパスワードの最新を抽出 */
	private WfmUserPassword getWfmUserPassword(Au0011Request req) {
		WfmUserPassword up = new WfmUserPasswordImpl();
		up.setCorporationCode(req.corporationCode);
		up.setUserCode(req.userCode);
		up.setDeleteFlag(DeleteFlag.OFF);
		up.setLockFlag(null);
		up.setChangeRequestFlag(null);

		GetWfmUserPasswordListInParam in = new GetWfmUserPasswordListInParam();
		in.setWfmUserPassword(up);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		List<WfmUserPassword> list = wf.getWfmUserPasswordList(in).getWfmUserPasswordList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	/**
	 * リセット＆新パスワードをメール送信
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse resetPassword(Au0011Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("ユーザコードが未指定です");

		// ユーザ情報
		WfmUser user = getUser(req);
		if (user == null)
			throw new NotFoundException("対象ユーザが見つかりません");

		// 新パスワードをメール送信する都合上、メールアドレスが必須である
		if (isEmpty(user.getMailAddress()))
			throw new InvalidUserInputException(MessageCd.MSG0237);

		// 既存パスワードを論理削除
		{
			final WfmUserPassword pswd = getWfmUserPassword(req);
			int count = repository.disableOldPassword(pswd, sessionHolder.getWfUserRole());
			log.debug("既存パスワードを{}件論理削除しました", count);
		}

		// 新パスワードを登録
		final int len = corpProp.getInt(CorporationProperty.MIN_PASSWORD_LENGTH);
		final String plainPassword = SecurityUtils.randomPassword(len);
		{
			// システムプロパティ：初回ログイン時にパスワード変更を強制するか？
			boolean changeRequest = corpProp.getBool(CorporationProperty.FIRST_LOGIN_CHANGE, false);

			final WfmUserPassword pswd = toWfmUserPassword(req);
			pswd.setChangeRequestFlag(changeRequest ? CommonFlag.ON : CommonFlag.OFF);
			pswd.setPassword(SecurityUtils.hash(plainPassword));

			final InsertWfmUserPasswordInParam in = new InsertWfmUserPasswordInParam();
			in.setWfmUserPassword(pswd);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmUserPasswordOutParam out = wf.insertWfmUserPassword(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}

		// メール送信
		{
			// 置換文字列Map
			final Map<String, String> variables = new HashMap<>();
			variables.put(MailVariables.LOGIN_ID, user.getUserAddedInfo());
			variables.put(MailVariables.LOGIN_USER_NAME, user.getUserName());
			variables.put(MailVariables.NEW_PASSWORD, plainPassword);

			// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
			final MailEntry entry = new MailEntry(
					user.getDefaultLocaleCode(), user.getMailAddress(), variables);
			final MailTemplate template = mail.toTemplate(
					MailTemplateFileName.RESET_PASSWORD, user.getCorporationCode());
			mail.send(template, entry);
		}

		final Au0011Response res = createResponse(Au0011Response.class, req);
		loadPasswordInfo(req, res);
		res.addSuccesses(i18n.getText(MessageCd.MSG0236));
		res.success = true;
		return res;
	}

	/** ユーザ情報を抽出 */
	private WfmUser getUser(Au0011Request req) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(req.corporationCode);
		in.setUserCode(req.userCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		List<WfmUser> list = wf.searchWfmUser(in).getUserList();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * アカウントロック
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse lockAccount(Au0011Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("ユーザコードが未指定です");

		final WfmUserPassword pswd = getWfmUserPassword(req);
		if (pswd == null)
			throw new NotFoundException("パスワードが存在しません");

		UpdateWfmUserPasswordInParam in = new UpdateWfmUserPasswordInParam();
		pswd.setLockFlag(LockFlag.ON);
		in.setWfmUserPassword(pswd);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmUserPasswordOutParam out = wf.updateWfmUserPassword(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Au0011Response res = createResponse(Au0011Response.class, req);
		loadPasswordInfo(req, res);
		res.addSuccesses(i18n.getText(MessageCd.MSG0238));
		res.success = true;
		return res;
	}

	/**
	 * アカウントロック解除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse unlockAccount(Au0011Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("ユーザコードが未指定です");

		final WfmUserPassword pswd = getWfmUserPassword(req);
		if (pswd == null)
			throw new NotFoundException("パスワードが存在しません");

		UpdateWfmUserPasswordInParam in = new UpdateWfmUserPasswordInParam();
		pswd.setLockFlag(LockFlag.OFF);
		pswd.setLoginNgCount(0L);
		in.setWfmUserPassword(pswd);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmUserPasswordOutParam out = wf.updateWfmUserPassword(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Au0011Response res = createResponse(Au0011Response.class, req);
		loadPasswordInfo(req, res);
		res.addSuccesses(i18n.getText(MessageCd.MSG0240));
		res.success = true;
		return res;
	}
}
