package jp.co.nci.iwf.component.mail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * メール送信クラス<br />
 */
class SendMail extends MiscUtils implements MailCodeBook {

	private static final String SMTP_AUTH_TRUE = "true";

	public void send(Mail mail) throws MessagingException, UnsupportedEncodingException, IOException {
		// *******************
		// メール送信準備
		// *******************
		Properties props = new Properties();
		// SMTPサーバの設定
		props.setProperty(PROPS_SMTP_HOST, mail.getSmtpName());
		props.setProperty(PROPS_HOST, mail.getSmtpName());		// Message-IDヘッダ生成用
		// TimeOut設定（SMTPサーバが倒れている場合用)
		props.setProperty(PROPS_CONNECT_TIMEOUT, Integer.parseInt(mail.getTimeOut())*1000 + "");
		// TimeOut設定（SMTP送信後のレスポンス待ち時間)
		props.setProperty(PROPS_TIMEOUT, Integer.parseInt(mail.getTimeOut())*1000 + "");
		// エラーメール設定		String from = defaults(mail.getReturnPath(), mail.getReplyTo(), mail.getFrom());
		if (isNotEmpty(from)) {
			props.setProperty(PROPS_FROM, from);
		}

		// SMTP認証の設定
		MailAuthenticator auth = null;
		if (SMTP_AUTH_TRUE.equalsIgnoreCase(mail.getSmtpAuth())) {
			// SMTP認証ありの場合のみ、以下の設定を行う
			props.setProperty(PROPS_SMTP_AUTH, mail.getSmtpAuth());
			props.setProperty(PROPS_SMTP_PORT , mail.getSmtpPort());
			props.setProperty(PROPS_TRANSPORT_PROTOCOL , mail.getTransportProtocol());
			props.setProperty(PROPS_SMTP_STARTTLS_ENABLE , mail.getSmtpStarttlsEnable());
			props.setProperty(PROPS_SMTP_ENABLESSL_ENABLE , mail.getSmtpEnableSSLEnable());
			props.setProperty(PROPS_SMTP_SOCKETFACTORY_CLASS , mail.getSmtpSocketfactoryClass());
			props.setProperty(PROPS_SMTP_SOCKETFACTORY_FALLBACK , mail.getSmtpSocketfactoryFallback());
			props.setProperty(PROPS_SMTP_SOCKETFACTORY_PORT,  mail.getSmtpPort());
			// STMP認証をする場合はユーザ名とパスワードを設定する
			auth = new MailAuthenticator(mail.getSmtpUser(), mail.getSmtpPassword());
		}

		Session session = Session.getDefaultInstance(props, auth);
		for (Object key : props.keySet()) {
			session.getProperties().put(key, props.get(key));
		}
		MimeMessage mimeMessage = new MimeMessage(session);

		// *******************
		// メールヘッダー作成
		// *******************
		setHeader(mimeMessage , mail);

		// *******************
		// メール本文作成
		// *******************
		setBody(mimeMessage , mail);

		// 送信
		Transport.send(mimeMessage);
	}

	/**
	 * <ul>
	 * ヘッダーを設定	 * </ul>
	 * @param mimeMessage
	 * @param mail
	 */
	private void setHeader(MimeMessage mimeMessage , Mail mail) throws MessagingException, UnsupportedEncodingException{

		// *******************
		// 送り先設定
		// *******************
		// 送信先メールアドレス
		if (mail.getTo() != null && mail.getTo().length > 0) {
			mimeMessage.setRecipients(Message.RecipientType.TO, this.convertMailAddress(mail.getTo(), null, mail.getEncode()));
		}
		// CCメールアドレス
		if (mail.getCc() != null && mail.getCc().length > 0) {
			mimeMessage.setRecipients(Message.RecipientType.CC, this.convertMailAddress(mail.getCc(), null, mail.getEncode()));
		}
		// BCCメールアドレス
		if (mail.getBcc() != null && mail.getBcc().length > 0) {
			mimeMessage.setRecipients(Message.RecipientType.BCC, this.convertMailAddress(mail.getBcc(), null, mail.getEncode()));
		}
		// 返信先メールアドレス
		if(isNotEmpty(mail.getReplyTo())) {
			mimeMessage.setReplyTo(this.convertMailAddress(mail.getReplyTo(), mail.getFromPersonal(), mail.getEncode()));
		}

		// *******************
		// 送信元設定
		// *******************
		// 送信元メールアドレス設定
		mimeMessage.addFrom(convertMailAddress(mail.getFrom(), mail.getFromPersonal(), mail.getEncode()));

		// 配送エラーの戻り先
		if(mail.getReturnPath() != null){
			mimeMessage.addHeader(HEADER_RETURN_PAHT, sanitizingCRLF(mail.getReturnPath()));
		}

		// *******************
		// メール内容固定部設定
		// *******************
		// メーラ名設定
		mimeMessage.setHeader(HEADER_X_MAILER, sanitizingCRLF(mail.getMailer()));
		// タイトル
		mimeMessage.setSubject(sanitizingCRLF(mail.getSubject()), mail.getEncode());

		// 返信先メールアドレス
		mimeMessage.setReplyTo(convertMailAddress(mail.getReplyTo(), mail.getFromPersonal(), mail.getEncode()));

		// 日付
		mimeMessage.setSentDate(new Date());
	}

	/**
	 * <ul>
	 * 本文、添付ファイルを設定
	 * </ul>
	 * @param mimeMessage
	 * @param mail
	 */
	private void setBody(MimeMessage mimeMessage , Mail mail) throws MessagingException, IOException{
		// 本文
		String content = mail.getContent();

		//本文を設定
		if (mail.getAttachmentFiles().isEmpty()) {

			//添付なし
			if (content != null) {
				mimeMessage.setText(content, mail.getEncode());
				mimeMessage.setHeader(HEADER_CONTENT_TRANSFER_ENCODING, mail.getContentTransferEncoding());
			}
		} else {

			//添付あり
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart body = new MimeBodyPart();


			List<MailAttachFile> attachmentFiles = mail.getAttachmentFiles();

			// 本文設定
			if (content != null) {
				body.setText(content, mail.getEncode());
				body.setHeader(HEADER_CONTENT_TRANSFER_ENCODING, mail.getContentTransferEncoding());
				multipart.addBodyPart(body);
			}

			// 添付ファイルの設定
			for (MailAttachFile attachFile : attachmentFiles) {
				MimeBodyPart attachBody = new MimeBodyPart();


				String fileName = attachFile.getFileName();
				try (BufferedInputStream in = attachFile.openInputStream()) {
					attachBody.setDataHandler(new DataHandler(new ByteArrayDataSource(in, CONTENT_TYPE)));
				}
				attachBody.setFileName(MimeUtility.encodeText(fileName, mail.getEncode(), "B"));

				multipart.addBodyPart(attachBody);
			}

			// マルチパートオブジェクトをメッセージに設定
			mimeMessage.setContent(multipart);
		}
	}

	/**
	 * <ul>
	 * MailAddressからInternetAddressに変換します.
	 * </ul>
	 * @param addresses
	 * @param encode
	 * @return internetAddresses
	 * @exception UnsupportedEncodingException
	 * @exception AddressException
	 */
	private InternetAddress[] convertMailAddress(String[] addresses , String[] personals, String encode) throws UnsupportedEncodingException{
		List<InternetAddress> addressList = new ArrayList<InternetAddress>();
		for(int i = 0; i < addresses.length; i++){
			String personal = personals != null && i < personals.length ? personals[i] : "";
			addressList.add(conv(addresses[i], personal, encode));
		}
		return addressList.toArray(new InternetAddress[addressList.size()]);
	}

	/**
	 * MailAddressからInternetAddressに変換します.
	 * @param addr
	 * @param personal
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private InternetAddress[] convertMailAddress(String addr , String personal, String encode) throws UnsupportedEncodingException{
		return new InternetAddress[] { conv(addr, personal, encode) };
	}

	private InternetAddress conv(String addr , String personal, String encode) throws UnsupportedEncodingException{
		return new InternetAddress(
				sanitizingCRLF(addr), sanitizingCRLF(personal), encode);
	}

    /**
     * ヘッダーに含まれる改行コードをサニタイジング
     *
     * @param value 変換対象文字列
     * @return 変換後文字列
     */
    private String sanitizingCRLF(String value) {
    	if (value != null) {
    		// 改行コードはCR+LF形式のみサニタイジング対象とする（RFC 2822 準拠 ※line feedは対象外）
    		return value.replaceAll("\r\n", "");
    	}
    	return value;
    }
}
