package jp.co.nci.iwf.jersey.exception;

/**
 * 排他ロックエラー（すでに他ユーザによって更新済み）を示す例外
 */
public class AlreadyUpdatedException extends PreconditionException {
	/** デフォルトコンストラクタ */
	public AlreadyUpdatedException() {
		super("");
	}

	/**
	 * コンストラクタ
	 * @param msg メッセージ
	 */
	public AlreadyUpdatedException(String msg) {
		super(msg);
	}

	/**
	 * コンストラクタ
	 * @param format メッセージの書式
	 * @param args メッセージのパラメータ
	 */
	public AlreadyUpdatedException(String format, Object...args) {
		super(String.format(format, args));
	}
}
