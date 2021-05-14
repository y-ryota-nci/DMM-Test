package jp.co.nci.iwf.jersey.exception;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;

/**
 * 前提条件が間違っている場合の抽象基底例外クラス
 */
public abstract class PreconditionException extends RuntimeException {
	/** デフォルト・コンストラクタ */
	public PreconditionException() {
	}

	/**
	 * コンストラクタ
	 * @param msg メッセージ
	 */
	public PreconditionException(String msg) {
		super(msg);
	}

	/**
	 * コンストラクタ
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public PreconditionException(MessageCd messageCd, Object...args) {
		super(toText(messageCd, args));
	}

	/**
	 * コンストラクタ
	 * @param e 例外
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public PreconditionException(Exception e, MessageCd messageCd, Object...args) {
		super(toText(messageCd, args), e);
	}

	/** MessageCdを多言語対応リソース文字列へ変換 */
	protected static String toText(MessageCd messageCd, Object...args) {
		I18nService i18n = CDI.current().select(I18nService.class).get();
		return i18n.getText(messageCd, args);
	}

}
