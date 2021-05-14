package jp.co.nci.iwf.component.mail;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * メール送信サービス
 *
 */
@RequestScoped
public class MailService extends BaseService implements MailCodeBook {

	/** メール環境設定 */
	@Inject
	protected MailConfig config;
	/** ロガー */
	@Inject
	protected Logger log;
	/** アクセスログサービス */
	@Inject
	protected AccessLogService accessLog;
	/** ルックアップサービス */
	@Inject
	protected MwmLookupService lookup;
	/** メールリポジトリ */
	@Inject
	protected MailRepository repository;
	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;
	/** メール変数サービス */
	@Inject private MailVariableService variableService;

	/**
	 * テンプレートファイル名から、テンプレートを読み込み
	 * @param mailTemplateFileName メールテンプレートのファイル名
	 * @param corporationCode 企業コード
	 */
	public MailTemplate toTemplate(String mailTemplateFileName, String corporationCode) {
		return new MailTemplate(mailTemplateFileName, corporationCode);
	}

	/**
	 * メール送信のメイン処理
	 * @param template メールテンプレート
	 * @param mailEntry 送信内容
	 * @param callback メール送信後に呼び出されるコールバック関数
	 */
	public void send(MailTemplate template, MailEntry mailEntry) {
		send(template, mailEntry, null);
	}

	/**
	 * メール送信のメイン処理
	 * @param template メールテンプレート
	 * @param mailEntry 送信内容
	 * @param callback メール送信後に呼び出されるコールバック関数
	 */
	public void send(MailTemplate template, MailEntry mailEntry, IMailCallback callback) {
		try {
			// SMTP設定
			this.config.validate();

			// メールテンプレートに送信内容を差し込んで、メール作成
			final Mail mail = create(mailEntry, template);
			final List<Mail> mails = new ArrayList<>();
			mails.add(mail);

			// メール内容ログ出力
			if (log.isDebugEnabled()) {
				logMails(mails);
			}

			// 本番／ステージング環境ならメールを実際に送信。
			switch (config.getMailEnv()) {
			case STAGING:
			case PRODUCT:
				// これ以降は非同期実行なので、スレッドに紐付いた情報や
				// トランザクションとは切り離されてしまうので注意！
				SendMailHelper.sendAsync(mails, mailEntry.getVariables(), callback, accessLog.loadAccessLogId());
				break;
			case DEVELOP:
				// 開発時はメール送信自体が行われないのでコールバックを独自に呼んでやる
				if (callback != null) {
					callback.onSent(mailEntry.getVariables(), mail, true);
				}
				break;
			}
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new MailException(e);
		}
	}

	/**
	 * メール送信内容をログ出力
	 * @param mails
	 */
	protected void logMails(List<Mail> mails) {
		final MailEnv mailEnv = config.getMailEnv();
		final StringBuilder sb = new StringBuilder(256);
		for (Mail mail : mails) {
			sb.append("--------------").append(LF);
			sb.append("To : ").append(Arrays.toString(mail.getTo())).append(LF);
			if (mail.getCc() != null && mail.getCc().length > 0) {
				sb.append("Cc : ").append(Arrays.toString(mail.getCc())).append(LF);
			}
			if (mail.getBcc() != null && mail.getBcc().length > 0) {
				sb.append("Bcc : ").append(Arrays.toString(mail.getBcc())).append(LF);
			}
			sb.append("From : ").append(mail.getFrom()).append(LF);
			sb.append("Subject : ").append(mail.getSubject()).append(LF);
//			sb.append("Encode : ").append(mail.getEncode()).append(LF);
//			sb.append("ContentTransferEncoding : ").append(mail.getContentTransferEncoding()).append(LF);

			sb.append("Attachment Files : ");
			if (mail.getAttachmentFiles().isEmpty()) {
				sb.append("---");
			} else {
				for (MailAttachFile f : mail.getAttachmentFiles()) {
					sb.append("[").append(f.getFileName()).append("] ");
				}
			}
			sb.append(LF);

			sb.append("Body : ").append(LF).append(mail.getContent());sb.append(LF);



		}
		log.debug("メール送信内容 ({}) START --- {}{}", mailEnv, LF, sb);
		log.debug("メール送信内容 ({}) END -----", mailEnv);
	}

	/**
	 * テンプレートやSMTP設定からメール送信内容を生成
	 * @param mailEntry メール送信先情報
	 * @param template メールテンプレート
	 * @param variables メールテンプレートの置換Map
	 * @param attachFiles 添付ファイル。FileResource／SqlResource／ByteResource／InputStreamResource として渡すこと
	 * @return
	 */
	protected Mail create(MailEntry mailEntry, MailTemplate template) throws IOException {
		// 送信先ユーザの言語コード
		final String localeCode = mailEntry.getLocaleCode();

		if (!template.isSupport(localeCode)) {
			throw new MailException(String.format("メールテンプレート[%s]に言語コード[%s]は未定義です", template.getFileName(), localeCode));
		}

		final Mail mail = new Mail();
		mail.setEncode(config.getEncode());	// メールHEADER＋BODYの文字エンコード
		mail.setContentTransferEncoding(config.getContentTransferEncoding());

		// 送信先
		if (MailEnv.STAGING == config.getMailEnv()) {
			// ステージング環境ではメール送信するが、常にダミーアドレスへの送信を行う
			// また、CC・BCCは送信しない
			mail.setTo(config.getDummySendTo());
		}
		else {
			// TO/CC/BCC
			mail.setTo(mailEntry.getToList());
			mail.setCc(mailEntry.getCcList());
			mail.setBcc(mailEntry.getBccList());
			// システムが自動付与するCC/BCC
			mail.addCc(config.getDefaultCc());
			mail.addBcc(config.getDefaultBcc());
			// テンプレートから付与されるTO/CC/BCC
			mail.addTo(template.getTo(localeCode));
			mail.addCc(template.getCc(localeCode));
			mail.addBcc(template.getBcc(localeCode));
		}

		// 件名（言語コードごと）
		final String subject = template.getSubject(localeCode);
		mail.setSubject(replaceVariables(subject, mailEntry.getVariables(), mailEntry.getLocaleCode(), template.getCorporationCode()));
		// 本文（言語コードごと、変数を置換）
		final String body = template.getContents(localeCode);
		mail.setContent(replaceVariables(body, mailEntry.getVariables(), mailEntry.getLocaleCode(), template.getCorporationCode()));
		// 添付ファイル
		mail.addAttachmentFiles(mailEntry.getAttachFiles());

		// 送信元情報（言語コードごと）
		// テンプレートに差出人があればそれを使うが、なければ環境設定から取得
		if (StringUtils.isNotEmpty(template.getFrom(localeCode))) {
			mail.setFrom(template.getFrom(localeCode));
			mail.setFromPersonal(template.getFromPersonal(localeCode));
		} else if (isNotEmpty(config.getFrom())) {
			mail.setFrom(config.getFrom());
			mail.setFromPersonal(config.getFromPersonal());
		}
		mail.setReturnPath(config.getReturnPath());
		mail.setReplyTo(config.getReplyTo());
		mail.setMailer(config.getMailer());

		// SMTPサーバ系パラメータ
		mail.setSmtpName(config.getHost());
		mail.setTimeOut(config.getTimeout());

		// SMTP認証系パラメタの設定
		mail.setSmtpAuth(config.getAuth());
		mail.setSmtpPort(config.getPort());
		mail.setSmtpUser(config.getUser());
		mail.setSmtpPassword(config.getPassword());
		mail.setTransportProtocol(config.getTransportProtocol());
		mail.setSmtpStarttlsEnable(config.getEnableStarttls());
		mail.setSmtpEnableSSLEnable(config.getEnableSSL());
		mail.setSmtpSocketfactoryClass(config.getSocketFactoryClass());
		mail.setSmtpSocketfactoryFallback(config.getSocketFactoryFallback());

		// メールテンプレート
		mail.setTemplateName(template.getFileName());

		return mail;
	}

	/**
	 * テンプレートのプレースホルダ文字列を置換
	 * @param source テンプレート文字列
	 * @param variables メールテンプレートの置換Map
	 * @return
	 */
	public String replaceVariables(String source, Map<String, String> variables, String localeCode, String corporationCode) {
		final StringBuffer sb = new StringBuffer(1024);
		if (isNotEmpty(source)) {
			// 置換Map
			final Map<String, String> all = new HashMap<>(Math.max(64, variables.size() * 2));

			// 指定されたメール変数
			variables.forEach((key, val) -> all.putIfAbsent(toKey(key), toVal(val)));

			// メール変数マスタを抽出してメール変数をエントリ
			variableService.getMwmMailVariables(corporationCode, localeCode)
				.forEach(e -> all.putIfAbsent(toKey(e.getMailVariableCode()), toVal(e.getMailVariableValue())));;

			// 操作者情報をメール変数としてエントリ
			createLoginInfoMap(localeCode, sessionHolder.getLoginInfo())
				.forEach((key, val) -> all.putIfAbsent(toKey(key), toVal(val)));;

			// テンプレートのプレースホルダを検索し、そのプレースホルダをキーとした値を探し出して置換
			final Pattern p = Pattern.compile("\\$\\{[a-zA-Z0-9_]+\\}");
			final Matcher m = p.matcher(source);
			while (m.find()) {
				String pattern = m.group();
				String value = all.get(pattern);
				m.appendReplacement(sb, quoteReplacement(value));
			}
			m.appendTail(sb);
		}
		return sb.toString();
	}

	/** 操作者情報をMap化 */
	private Map<String, String> createLoginInfoMap(String localeCode, LoginInfo login) {
		final Map<String, String> map = new HashMap<>();
		map.put(MailVariables.STAGING_ENV_STRING, config.getStagingEnvString());

		if (login != null) {
			if (eq(login.getLocaleCode(), localeCode)) {
				map.put(MailVariables.LOGIN_ID, login.getUserAddedInfo());
				map.put(MailVariables.LOGIN_USER_NAME, login.getUserName());
			} else {
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

				final WfvUserBelong ub = wf.searchWfvUserBelong(in).getUserBelongList().get(0);
				map.put(MailVariables.LOGIN_CORPORATION_NAME, ub.getCorporationName());
				map.put(MailVariables.LOGIN_ORGANIZATION_NAME, ub.getOrganizationName());
				map.put(MailVariables.LOGIN_POST_NAME, ub.getPostName());
				map.put(MailVariables.LOGIN_ID, ub.getUserAddedInfo());
				map.put(MailVariables.LOGIN_USER_NAME, ub.getUserName());
			}
		}
		return map;
	}

	/**
	 * 業務管理項目名称マスタを抽出し、その業務管理項目コードに一致する属性値をエンティティから抜き出してMap化。
	 * ようは業務管理コードを getterメソッド名としてエンティティから値を抜き出すってことだ。
	 * @param entities メール変数の値の元となるエンティティ。先頭にあるものから優先して判定していく
	 * @return
	 */
	public Map<String, String> createVariables(String corporationCode, Object...entities) {
		// 業務管理項目マスタを抽出
		final Map<String, String> map = new HashMap<>();
		repository.getBusinessInfoName(corporationCode).forEach(e -> {
			// 業務管理コードをキャメルケース化してgetterメソッド名のベースとする
			String propName = toCamelCase(e.getBusinessInfoCode());

			String val = null;
			for (Object entity : entities) {
				if (entity == null) continue;
				// エンティティのgetterメソッドを蹴飛ばして値を抜き出す
				final PropertyDescriptor pd = getPropertyDescriptor(entity, propName);
				if (pd != null && pd.getReadMethod() != null) {
					Object obj = getPropertyValue(entity, pd);
					val = obj == null ? "" : obj.toString();
					break;
				}
			}
			map.put(e.getBusinessInfoCode(), val);
		});
		return map;
	}

	/**
	 * String.replaceAll()での検索パターン文字列を付与する
	 * @param v
	 * @return
	 */
	protected String toKey(String v) {
		return "$" + "{" + v + "}";
	}

	/**
	 * String.replaceAll()での検索パターン文字列を付与する
	 * @param val
	 * @return
	 */
	protected String toVal(String val) {
		return defaults(val, "");
	}
}
