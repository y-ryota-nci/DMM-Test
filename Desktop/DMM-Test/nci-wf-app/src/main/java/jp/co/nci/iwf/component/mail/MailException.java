package jp.co.nci.iwf.component.mail;

import javax.ws.rs.InternalServerErrorException;

/**
 * メール送信による例外
 */
public class MailException extends InternalServerErrorException {

	public MailException() {}

	public MailException(String msg) {
		super(msg);
	}

	public MailException(String msg, Object...args) {
		super(String.format(msg, args));
	}

	public MailException(String msg, Throwable t) {
		super(msg, t);
	}

	public MailException(Throwable t) {
		super(t);
	}
}
