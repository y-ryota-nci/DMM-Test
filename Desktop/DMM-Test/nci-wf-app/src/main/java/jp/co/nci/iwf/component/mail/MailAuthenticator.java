package jp.co.nci.iwf.component.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * メール認証情報
 *
 *
 */
class MailAuthenticator extends Authenticator {
	/** ユーザ名 */
	private String userName;
	/** パスワード */
	private String password;

	/**
	 * コンストラクタ
	 * @param userName ユーザ名
	 * @param password パスワード
	 */
	public MailAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 認証情報取得
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.userName, this.password);
	}

}
