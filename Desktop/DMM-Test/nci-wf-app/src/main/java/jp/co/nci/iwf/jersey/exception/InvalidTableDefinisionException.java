package jp.co.nci.iwf.jersey.exception;

import jp.co.nci.iwf.component.MessageCd;

/**
 * テーブル定義が正しくない場合にスローされる例外
 */
public class InvalidTableDefinisionException extends PreconditionException {
	/** デフォルト・コンストラクタ */
	public InvalidTableDefinisionException() {
	}


	/**
	 * コンストラクタ
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidTableDefinisionException(Exception e, MessageCd messageCd) {
		super(e, messageCd);
	}

	/**
	 * コンストラクタ
	 * @param e 例外
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidTableDefinisionException(Exception e, MessageCd messageCd, Object...args) {
		super(e, messageCd, args);
	}
}
