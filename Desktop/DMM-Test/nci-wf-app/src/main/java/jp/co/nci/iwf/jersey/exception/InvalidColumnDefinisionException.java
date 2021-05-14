package jp.co.nci.iwf.jersey.exception;

import jp.co.nci.iwf.component.MessageCd;

/**
 * テーブルのカラム定義が正しくない場合にスローされる例外
 */
public class InvalidColumnDefinisionException extends PreconditionException {

	/** デフォルト・コンストラクタ */
	public InvalidColumnDefinisionException() {
	}


	/**
	 * コンストラクタ
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidColumnDefinisionException(Exception e, MessageCd messageCd) {
		super(e, messageCd);
	}

	/**
	 * コンストラクタ
	 * @param e 例外
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidColumnDefinisionException(Exception e, MessageCd messageCd, Object...args) {
		super(e, messageCd, args);
	}
}
