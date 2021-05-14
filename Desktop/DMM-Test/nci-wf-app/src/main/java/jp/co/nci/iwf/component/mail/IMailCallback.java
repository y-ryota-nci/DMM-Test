package jp.co.nci.iwf.component.mail;

import java.util.Map;

/**
 * メール送信後に呼び出されるコールバック
 */
public interface IMailCallback {
	/**
	 * メール送信成功時に呼び出されるコールバック関数
	 * @param variables 送信時の「メールテンプレートの置換Map」
	 * @param mail 実際にメール送信された内容（メール本文なら mail.getContents()で取得できる）
	 * @param success メール送信に成功すればtrue
	 */
	public void onSent(Map<String, String> variables, Mail mail, boolean success);

	/**
	 * リソース解放
	 */
	public void dispose();
}
