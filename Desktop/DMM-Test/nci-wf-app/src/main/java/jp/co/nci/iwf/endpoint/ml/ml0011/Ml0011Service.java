package jp.co.nci.iwf.endpoint.ml.ml0011;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplateFile;
import jp.co.nci.iwf.component.mail.MailVariableService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;
import jp.co.nci.iwf.jpa.entity.wm.WfmLocaleV;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * メールテンプレート編集のサービス
 */
@BizLogic
public class Ml0011Service extends BaseService {
	@Inject private Ml0011Repository repository;
	@Inject private MailService mailService;
	@Inject private WfInstanceWrapper wf;
	@Inject private MailVariableService variableService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ml0011InitResponse init(Ml0011InitRequest req) {
		if (req.fileId == null)
			throw new BadRequestException("メールテンプレートファイルIDが未指定です");
		if (req.headerId != null && req.version == null)
			throw new BadRequestException("バージョンが未指定です");
		if (req.headerId == null && req.version != null)
			throw new BadRequestException("メールテンプレートヘッダIDが未指定です");

		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Ml0011InitResponse res = createResponse(Ml0011InitResponse.class, req);

		// メールテンプレートのヘッダ
		res.header = repository.getHeader(req.fileId, corporationCode, localeCode);

		// 排他チェック
		if (req.version != null && !eq(req.version, res.header.version))
			throw new AlreadyUpdatedException();

		if (isEmpty(res.header.corporationCode))
			res.header.corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// ファイルテンプレートを読み込む。
		// 「標準へ戻す」といったら、このファイルテンプレートへ戻すということだ
		final MailTemplateFile fileTemplate = new MailTemplateFile(
				localeService.getSelectableLocaleCodes(), res.header.mailTemplateFilename, "UTF-8");

		// ヘッダ：標準に戻す
		if (req.backToStandard) {
			res.header.sendFrom = fileTemplate.getFrom(localeCode);
			res.header.sendBcc = null;
			res.header.sendCc = null;
			res.header.sendTo = null;
			res.header.returnTo = null;
		}

		// メールテンプレートの本文
		res.bodies = createBodies(req, res.header, fileTemplate);

		// 「標準へ戻す」ことが可能か？
		res.canBackToStandard = !fileTemplate.isEmpty();

		// メール変数の選択肢
		res.variables = createOptionItems();

		res.success = true;
		return res;
	}

	/**  メールテンプレートの本文を抽出 */
	private List<Ml0011EntityBody> createBodies(Ml0011InitRequest req,
			final Ml0011EntityHeader header, final MailTemplateFile fileTemplate) {
		// 既存の本文を抽出
		final Map<String, Ml0011EntityBody> map = repository.getBody(req.headerId)
				.stream()
				.collect(Collectors.toMap(b -> b.localeCode, b -> b));

		// 既存の本文データに対象ロケールがなければ空インスタンスを生成し、必ず全ロケール分の本文があることを保証する
		final List<Ml0011EntityBody> bodies = new ArrayList<>();
		for (WfmLocaleV locale : localeService.getWfmLocale(sessionHolder.getLoginInfo().getLocaleCode())) {
			Ml0011EntityBody body = map.get(locale.getLocaleCode());
			if (body == null) {
				body = new Ml0011EntityBody();
				body.localeCode = locale.getLocaleCode();
				body.mailTemplateHeaderId = header.mailTemplateHeaderId;
			}
			body.localeName = locale.getLocaleName();

			// 「標準へ戻す」際はファイルテンプレートから読み直す
			if (req.backToStandard) {
				// ファイルテンプレートがあればそちらから読み込む
				if (fileTemplate.isSupport(body.localeCode, fileTemplate.getFileName())) {
					body.mailSubject = fileTemplate.getSubject(body.localeCode);
					body.mailBody = fileTemplate.getContents(body.localeCode);
				}
			}
			bodies.add(body);
		}
		return bodies;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ml0011SaveResponse save(Ml0011SaveRequest req) {
		if (req.header == null)
			throw new BadRequestException("メールテンプレートヘッダ情報がありません");
		if (req.bodies == null || req.bodies.isEmpty())
			throw new BadRequestException("メールテンプレート本文情報がありません");

		final Ml0011SaveResponse res = createResponse(Ml0011SaveResponse.class, req);

		// バリデーション
		final String error = validate(req);

		if (isEmpty(error)) {
			// ヘッダの差分更新
			Long headerId = req.header.mailTemplateHeaderId;
			{
				MwmMailTemplateHeader current = (headerId == null)
						? null : repository.getMwmMailTemplateHeader(headerId);
				if (current == null)
					headerId = repository.insert(req.header);
				else
					repository.update(current, req.header);
			}
			// 本文の差分更新
			{
				final Map<Long, MwmMailTemplateBody> currents = repository
						.getMwmMailTemplateBody(headerId)
						.stream()
						.collect(Collectors.toMap(b -> b.getMailTemplateBodyId(), b -> b));
				for (Ml0011EntityBody input : req.bodies) {
					// 既存リストから除去するものがなければ新規、あれば既存を更新
					MwmMailTemplateBody current = currents.remove(input.mailTemplateBodyId);
					if (current == null)
						repository.insert(headerId, input);
					else
						repository.update(current, input);
				}
				for (MwmMailTemplateBody body : currents.values())
					repository.delete(body);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.mailTemplate));
			res.success = true;
		}
		else {
			res.addAlerts(error);
		}
		// 処理結果
		return res;
	}

	/** バリデーション */
	private String validate(Ml0011SaveRequest req) {
		// CC
		if (!ValidatorUtils.maxbyteUTF8(req.header.sendCc, 300))
			return i18n.getText(MessageCd.MSG0093, 300);
		// BCC
		if (!ValidatorUtils.maxbyteUTF8(req.header.sendBcc, 300))
			return i18n.getText(MessageCd.MSG0093, 300);
		// 送信者
		if (!ValidatorUtils.maxbyteUTF8(req.header.sendFrom, 300))
			return i18n.getText(MessageCd.MSG0093, 300);
		// 返信先
		if (!ValidatorUtils.maxbyteUTF8(req.header.returnTo, 300))
			return i18n.getText(MessageCd.MSG0093, 300);

		final int MAX_SUBJECT = 300;
		final int MAX_BODY = 9000;
		for (Ml0011EntityBody body : req.bodies) {
			// 件名
			if (!ValidatorUtils.maxbyteUTF8(body.mailSubject, MAX_SUBJECT))
				return i18n.getText(MessageCd.MSG0093, MAX_SUBJECT);
			// 本文
			if (!ValidatorUtils.maxbyteUTF8(body.mailBody, MAX_BODY))
				return i18n.getText(MessageCd.MSG0093, MAX_BODY);
		}

		return null;
	}

	/**
	 * プレビュー
	 * @param req
	 * @return
	 */
	public Ml0011PreviewResponse preview(Ml0011PreviewRequest req) {
		if (isEmpty(req.localeCode))
			throw new BadRequestException("言語コードが未指定です");

		String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		Map<String, String> fixedValues = variableService.getMwmMailVariables(corporationCode, localeCode)
				.stream()
				.collect(Collectors.toMap(
						v -> "$" + "{" + v.getMailVariableCode() + "}",
						v -> v.getMailVariableValue()));

		final Ml0011PreviewResponse res = createResponse(Ml0011PreviewResponse.class, req);
		res.mailSubject = replaceVariable(req.mailSubject, req.localeCode, fixedValues);
		res.mailBody = replaceVariable(req.mailBody, req.localeCode, fixedValues);
		res.success = true;
		return res;
	}

	/** メールテンプレートのメール変数の選択肢を抽出 */
	private List<OptionItem> createOptionItems() {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final List<OptionItem> items = new ArrayList<>();
		items.add(new OptionItem(MailVariables.STAGING_ENV_STRING, i18n.getText(MessageCd.stagingEnvString)));

		// ログイン者情報
		items.add(new OptionItem(MailVariables.LOGIN_CORPORATION_NAME, i18n.getText(MessageCd.operatorCorporationName)));
		items.add(new OptionItem(MailVariables.LOGIN_ID, i18n.getText(MessageCd.operatorLoginId)));
		items.add(new OptionItem(MailVariables.LOGIN_USER_NAME, i18n.getText(MessageCd.operatorLoginName)));
		items.add(new OptionItem(MailVariables.LOGIN_ORGANIZATION_NAME, i18n.getText(MessageCd.operatorOrganizationName)));
		items.add(new OptionItem(MailVariables.LOGIN_POST_NAME, i18n.getText(MessageCd.operatorPostName)));
		items.add(new OptionItem(MailVariables.ATTACH_FILE, i18n.getText(MessageCd.attachFile)));
		items.add(new OptionItem(MailVariables.COMMENT, i18n.getText(MessageCd.comment)));
		items.add(new OptionItem(MailVariables.PROCESS_DEF_NAME, i18n.getText(MessageCd.processDefName)));
		items.add(new OptionItem(MailVariables.SCREEN_PROCESS_NAME, i18n.getText(MessageCd.screenProcessName)));
		items.add(new OptionItem(MailVariables.TO_USER_NAME, i18n.getText(MessageCd.toUserName)));
		items.add(new OptionItem(MailVariables.TO_USER_ADDED_INFO, i18n.getText(MessageCd.toUserAddedInfo)));
		items.add(new OptionItem(MailVariables.FROM_USER_NAME, i18n.getText(MessageCd.fromUserName)));
		items.add(new OptionItem(MailVariables.FROM_USER_ADDED_INFO, i18n.getText(MessageCd.fromUserAddedInfo)));
		items.add(new OptionItem(MailVariables.USER_NAME_OPERATION_START, i18n.getText(MessageCd.userNameOperationStart)));
		items.add(new OptionItem(MailVariables.ORGANIZATION_NAME_START, i18n.getText(MessageCd.organizationNameStart)));
		items.add(new OptionItem(MailVariables.CORPORATION_CODE, i18n.getText(MessageCd.corporationCode)));
		items.add(new OptionItem(MailVariables.PROCESS_ID, i18n.getText(MessageCd.processId)));
		items.add(new OptionItem(MailVariables.ACTIVITY_ID, i18n.getText(MessageCd.activityId)));
		items.add(new OptionItem(MailVariables.TIMESTAMP, i18n.getText(MessageCd.timestampUpdated)));

		// メール変数マスタ
		items.addAll(variableService.getOptionItems(corporationCode, localeCode, false));

		// 業務管理項目
		repository.getMwmBusinessInfoName(corporationCode, localeCode).forEach(e -> {
			items.add(new OptionItem(e.getBusinessInfoCode(), e.getBusinessInfoName()));
		});

		return items;
	}

	/**  */
	private String replaceVariable(String str, String localeCode, Map<String, String> fixedValues) {

		// ログイン者情報を指定言語コードで抽出
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(login.getCorporationCode());
		in.setUserCode(login.getUserCode());
		in.setOrganizationCode(login.getOrganizationCode());
		in.setPostCode(login.getPostCode());
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setLanguage(localeCode);
		final List<WfvUserBelongImpl> belongs = wf.searchWfvUserBelong(in).getUserBelongList();
		if (belongs.isEmpty())
			throw new InternalServerErrorException("操作者のユーザ所属情報が見つかりません");
		final WfvUserBelong ub = belongs.get(0);

		// 文面からメール変数を抜き出してダミー値を割り当てる
		final Map<String, String> variables = new HashMap<>(512);
		final Pattern p = Pattern.compile("\\$\\{[a-zA-Z0-9_]+\\}");
		final Matcher m = p.matcher(str);
		while (m.find()) {
			String pattern = m.group();
			String key = pattern.substring(2, pattern.length() - 1);
			String value = getDummyString(pattern, localeCode, ub, fixedValues);
			if (value != null)
				variables.put(key, value);
		}
		// メール変数を差し替え
		return mailService.replaceVariables(str, variables, localeCode, login.getCorporationCode());
	}

	/** プレビュー表示用ダミー文字列を返す */
	private String getDummyString(String pattern, String localeCode, WfvUserBelong ub, Map<String, String> fixedValues) {
		// 通常はメールテンプレートを使用する側が固定的にメール変数を差し替えるものは、ハードコードでダミー値を設定
		final String key = (pattern == null ? "" : pattern.toUpperCase());
		if (fixedValues.containsKey(key))
			return fixedValues.get(key);
		if (key.contains(MailVariables.AMOUNT))
			return "123456789";
		if (key.contains(MailVariables.APPLICATION_NO))
			return "APPLY-12345";
		if (key.contains(MailVariables.APPROVAL_NO))
			return "APPROVAL-12345";
		if (key.contains(MailVariables.SUBJECT))
			return i18n.getText(localeCode, MessageCd.title);
		if (key.contains(MailVariables.ATTACH_FILE))
			return i18n.getText(localeCode, MessageCd.notExist);
		if (key.contains(MailVariables.PROCESS_DEF_NAME))
			return i18n.getText(localeCode, MessageCd.processDefName);
		if (key.contains(MailVariables.SCREEN_PROCESS_NAME))
			return i18n.getText(localeCode, MessageCd.screenProcessName);
		if (key.contains(MailVariables.COMMENT))
			return i18n.getText(localeCode, MessageCd.comment);
		if (key.contains(MailVariables.TIMESTAMP))
			return String.valueOf(now().getTime());
		if (key.contains(MailVariables.PROCESS_ID))
			return "123";
		if (key.contains(MailVariables.ACTIVITY_ID))
			return "456";
		if (key.contains(MailVariables.TEMPORARY_PASSWORD))
			return SecurityUtils.randomPincode(4);
		if (key.contains(MailVariables.NEW_PASSWORD))
			return SecurityUtils.randomPassword(8);
		if (key.contains(MailVariables.CIPHER))
			return SecurityUtils.randomToken(32);
		if (key.contains(MailVariables.HOURS))
			return String.valueOf(24);

		// ユーザ／組織／役職に関連するものは操作者情報をダミー値とする
		if (key.contains("CORPORATION_CODE"))
			return ub.getCorporationCode();
		if (key.contains("USER_ADDED_INFO"))
			return ub.getUserAddedInfo();
		if (key.contains("USER_CODE"))
			return ub.getUserCode();
		if (key.contains("USER_NAME"))
			return ub.getUserName();
		if (key.contains("USER_NAME_ABBR"))
			return ub.getUserName();
		if (key.contains("ORGANIZATION_CODE"))
			return ub.getOrganizationCode();
		if (key.contains("ORGANIZATION_ADDED_INFO"))
			return ub.getOrganizationAddedInfo();
		if (key.contains("ORGANIZATION_NAME"))
			return ub.getOrganizationAddedInfo();
		if (key.contains("CORPORATION_CODE"))
			return ub.getCorporationCode();
		if (key.contains("CORPORATION_NAME"))
			return ub.getCorporationName();
		if (key.contains("CORPORATION_GROUP_CODE"))
			return ub.getCorporationGroupCode();
		if (key.contains("POST_CODE"))
			return ub.getPostCode();
		if (key.contains("POST_ADDED_INFO"))
			return ub.getPostAddedInfo();
		if (key.contains("POST_NAME"))
			return ub.getPostName();
		if (key.endsWith("_DATE}"))
			return toStr(today(), "yyyy/MM/dd");
		if (key.endsWith("_ID}"))
			return "1234";

		// この他はメールサービス側がルックアップから抜き出してくるので、これ以上は頑張らない
		return null;
	}
}
