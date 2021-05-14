package jp.co.nci.iwf.jersey.exception;

import jp.co.nci.iwf.component.MessageCd;

/**
 * バリデーションエラーを示す例外。
 * ロールバックはしたいけど、エラー画面へ遷移せずエラーメッセージだけ表示したい場合に使用してください。
 */
public class InvalidUserInputException extends PreconditionException {
	/** デフォルト・コンストラクタ */
	public InvalidUserInputException() {
	}

	/**
	 * コンストラクタ
	 * @param msg エラーメッセージ文字列
	 */
	public InvalidUserInputException(String msg) {
		super(msg);
	}

	/**
	 * コンストラクタ
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidUserInputException(MessageCd messageCd, Object...args) {
		super(messageCd, args);
	}

	/**
	 * コンストラクタ
	 * @param e 例外
	 * @param messageCd メッセージCD
	 * @param args パラメータ配列
	 */
	public InvalidUserInputException(Exception e, MessageCd messageCd, Object...args) {
		super(e, messageCd, args);
	}
}
