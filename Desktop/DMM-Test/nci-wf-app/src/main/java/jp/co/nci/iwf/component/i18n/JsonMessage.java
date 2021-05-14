package jp.co.nci.iwf.component.i18n;

import java.io.Serializable;

/**
 * JSON用メッセージエンティティ
 */
public class JsonMessage implements Serializable {
	public JsonMessage(String messageId, String text, String localeCode) {
		this.messageCd = messageId;
		this.text = text;
		this.localeCode = localeCode;
	}

	/** メッセージCD */
	public String messageCd;
	/** メッセージCDに対応した文言 */
	public String text;
	/** 言語 */
	public String localeCode;
}
