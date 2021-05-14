package jp.co.nci.iwf.component.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * メール情報クラス。
 * メール送信内容＋テンプレート＋メール環境情報をマージして作成される。
 */
public class Mail {
	/** 言語コード */
	private String localeCode;
	/** SMTP名 */
	private String smtpName = null;
	/** To */
	private List<String> to = null;
	/** cc */
	private List<String> cc = null;
	/** Bcc */
	private List<String> bcc = null;
	/** from */
	private String from = null;
	/** 差出人名 */
	private String fromPersonal;
	/** replyTo */
	private String replyTo = null;
	/** returnPath*/
	private String returnPath = null;
	/** subject */
	private String subject = null;
	/** content */
	private String content = null;
	/** attachmentFiles */
	private List<MailAttachFile> attachmentFiles = null;
	/** timeout */
	private String timeOut = null;
	/** mailer */
	private String mailer = null;
	/** encode */
	private String encode = null;
	/** contentTransferEncoding */
	private String contentTransferEncoding = null;
	/** テンプレート名 */
	private String templateName;

	/** SMTP認証 true / false */
	private String smtpAuth = null;
	/**  ポート */
	private String smtpPort = null;
	/**  ユーザID */
	private String smtpUser = null;
	/**  パスワード */
	private String smtpPassword = null;
	/**  プロトコル (smpts) */
	private String transportProtocol = null;
	/**  TLS */
	private String smtpStarttlsEnable = null;
	/**  SSL */
	private String smtpEnableSSLEnable = null;
	/**  SSL SocketFactory */
	private String smtpSocketfactoryClass = null;
	/**  SSL SocketFactory fallback */
	private String smtpSocketfactoryFallback = null;


	/**
	 * <ul>
	 * コンストラクタ.
	 * </ul>
	 */
	public Mail() {
		super();
		to = new ArrayList<String>();
		cc = new ArrayList<String>();
		bcc = new ArrayList<String>();
		attachmentFiles = new ArrayList<MailAttachFile>();
		timeOut = MailCodeBook.DEFAULT_TIME_OUT;
		encode = MailCodeBook.DEFAULT_ENCODE;
		contentTransferEncoding = MailCodeBook.DEFAULT_CONTENT_TRANSFER_ENCODEING;
		smtpAuth = MailCodeBook.DEFAULT_SMTP_AUTH;
	}

	/**
	 * toを返却する.
	 * @return to
	 */
	public String[] getTo() {
		return to.toArray(new String[to.size()]);
	}

	/**
	 * toを設定する.
	 * @param addresses
	 */
	public void addTo(String... addresses) {
		if (addresses != null) {
			for (String addr : addresses) {
				to.add(addr);
			}
		}
	}

	/**
	 * toを設定する.
	 * @param addresses
	 */
	public void addTo(Collection<String> addresses) {
		if (addresses != null)
			to.addAll(addresses);
	}

	/**
	 * toを設定する.
	 * @param address
	 */
	public void setTo(String...address) {
		to.clear();
		if (address != null)
			this.addTo(address);
	}

	/**
	 * toを設定する.
	 * @param addresses
	 */
	public void setTo(Collection<String> addresses) {
		to.clear();
		if (addresses != null)
			this.addTo(addresses);
	}

	/**
	 * ccを返却する.
	 * @return cc
	 */
	public String[] getCc() {
		return cc.toArray(new String[cc.size()]);
	}

	/**
	 * ccを設定する.
	 * @param addresses
	 */
	public void addCc(String... addresses) {
		if (addresses != null) {
			for (String addr : addresses) {
				cc.add(addr);
			}
		}
	}

	/**
	 * ccを設定する.
	 * @param addresses
	 */
	public void addCc(Collection<String> addresses) {
		if (addresses != null)
			cc.addAll(addresses);
	}

	/**
	 * ccを設定する.
	 * @param address
	 */
	public void setCc(String address) {
		cc.clear();
		if (address != null)
			addCc(address);
	}

	/**
	 * ccを設定する.
	 * @param addresses
	 */
	public void setCc(Collection<String> addresses) {
		cc.clear();
		if (addresses != null)
			addCc(addresses);
	}

	/**
	 * bccを返却する.
	 * @return bcc
	 */
	public String[] getBcc() {
		return this.bcc.toArray(new String[bcc.size()]);
	}

	/**
	 * bccを設定する.
	 * @param addresses
	 */
	public void addBcc(String... addresses) {
		if (addresses != null) {
			for (String addr : addresses) {
				bcc.add(addr);
			}
		}
	}

	/**
	 * bccを設定する.
	 * @param addresses
	 */
	public void addBcc(Collection<String> addresses) {
		if (addresses != null)
			bcc.addAll(addresses);
	}

	/**
	 * bccを設定する.
	 * @param address
	 */
	public void setBcc(String... addresses) {
		bcc.clear();
		if (addresses != null)
			this.addBcc(addresses);
	}

	/**
	 * bccを設定する.
	 * @param addresses
	 */
	public void setBcc(Collection<String> addresses) {
		bcc.clear();
		if (addresses != null)
			bcc.addAll(addresses);
	}

	/**
	 * fromを返却する.
	 * @return from
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * fromを設定する.
	 * @param address
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/** 差出人名 */
	public String getFromPersonal() {
		return fromPersonal;
	}
	/** 差出人名 */
	public void setFromPersonal(String fromPersonal) {
		this.fromPersonal = fromPersonal;
	}

	/**
	 * replyToを返却する.
	 * @return replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * replyToを設定する.
	 * @param address
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * subjectを返却する.
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * subjectを設定する.
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * contentを返却する.
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * contentを設定する.
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * mailerを返却する.
	 * @return mailer
	 */
	public String getMailer() {
		return mailer;
	}

	/**
	 * mailerを設定する.
	 * @param mailer
	 */
	public void setMailer(String mailer) {
		this.mailer = mailer;
	}

	/**
	 * attachmentFilesを返却する.
	 * @return attachmentFiles
	 */
	public List<MailAttachFile> getAttachmentFiles() {
		return attachmentFiles;
	}

	/**
	 * attachmentFilesを設定する.
	 * @param attachmentFiles
	 */
	public void addAttachmentFiles(Collection<MailAttachFile> attachmentFiles) {
		if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
			this.attachmentFiles.addAll(attachmentFiles);
		}
	}

	/**
	 * attachmentFilesを設定する.
	 * @param attachmentFiles
	 */
	public void addAttachmentFiles(MailAttachFile attachmentFiles) {
		if (attachmentFiles != null) {
			this.attachmentFiles.add(attachmentFiles);
		}
	}

	/**
	 * smtpNameを返却する.
	 * @return smtpName
	 */
	public String getSmtpName() {
		return smtpName;
	}

	/**
	 * smtpNameを設定する.
	 * @param smtpName
	 */
	public void setSmtpName(String smtpName) {
		this.smtpName = smtpName;
	}

	/**
	 * timeOutを返却する.
	 * @return timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * timeOutを設定する.
	 * @param timeOut
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * encodeを返却する.
	 * @return encode
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * encodeを設定する.
	 * @param encode
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * returnPathを返却する.
	 * @return returnPath
	 */
	public String getReturnPath() {
		return returnPath;
	}

	/**
	 * returnPathを設定する.
	 * @param returnPath
	 */
	public void setReturnPath(String returnPath) {
		this.returnPath = returnPath;
	}

	/**
	 * contentTransferEncodingを返却する.
	 * @return
	 */
	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	/**
	 * contentTransferEncodingを設定する.
	 * @param contentTransferEncoding
	 */
	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}


	/**
	 * @return smtpAuth
	 */
	public String getSmtpAuth() {
		return smtpAuth;
	}

	/**
	 * @param smtpAuth セットする smtpAuth
	 */
	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	/**
	 * @return smtpPort
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort セットする smtpPort
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * @return smtpUser
	 */
	public String getSmtpUser() {
		return smtpUser;
	}

	/**
	 * @param smtpUser セットする smtpUser
	 */
	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	/**
	 * @return smtpPassword
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * @param smtpPassword セットする smtpPassword
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	/**
	 * @return transportProtocol
	 */
	public String getTransportProtocol() {
		return transportProtocol;
	}

	/**
	 * @param transportProtocol セットする transportProtocol
	 */
	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	/**
	 * @return smtpStarttlsEnable
	 */
	public String getSmtpStarttlsEnable() {
		return smtpStarttlsEnable;
	}

	/**
	 * @param smtpStarttlsEnable セットする smtpStarttlsEnable
	 */
	public void setSmtpStarttlsEnable(String smtpStarttlsEnable) {
		this.smtpStarttlsEnable = smtpStarttlsEnable;
	}

	/**
	 * @return smtpEnableSSLEnable
	 */
	public String getSmtpEnableSSLEnable() {
		return smtpEnableSSLEnable;
	}

	/**
	 * @param smtpEnableSSLEnable セットする smtpEnableSSLEnable
	 */
	public void setSmtpEnableSSLEnable(String smtpEnablesslEnable) {
		this.smtpEnableSSLEnable = smtpEnablesslEnable;
	}

	/**
	 * @return smtpSocketfactoryClass
	 */
	public String getSmtpSocketfactoryClass() {
		return smtpSocketfactoryClass;
	}

	/**
	 * @param smtpSocketfactoryClass セットする smtpSocketfactoryClass
	 */
	public void setSmtpSocketfactoryClass(String smtpSocketfactoryClass) {
		this.smtpSocketfactoryClass = smtpSocketfactoryClass;
	}

	/**
	 * @return smtpSocketfactoryFallback
	 */
	public String getSmtpSocketfactoryFallback() {
		return smtpSocketfactoryFallback;
	}

	/**
	 * @param smtpSocketfactoryFallback セットする smtpSocketfactoryFallback
	 */
	public void setSmtpSocketfactoryFallback(String smtpSocketfactoryFallback) {
		this.smtpSocketfactoryFallback = smtpSocketfactoryFallback;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/** 当クラスのMap表現を返す */
	public Map<String, Object> toMap() {
		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("subject", getSubject());
		map.put("to", getTo());
		map.put("cc", getCc());
		map.put("bcc", getBcc());
		map.put("from", getFrom());
		map.put("localeCode", getLocaleCode());
		map.put("attachFile", getAttachmentFiles().stream()
				.map(af -> af.getFileName())
				.collect(Collectors.toList()));
		map.put("host", getSmtpName());
		map.put("templateName", getTemplateName());
		return map;
	}
}
