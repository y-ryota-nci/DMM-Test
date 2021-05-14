package jp.co.nci.iwf.jpa.eclipselink;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EclipseLinkのロガー。余計なものは何も出力しない
 */
public class EclipseLinkLogger extends AbstractSessionLog implements SessionLog {
	private static final Logger log = LoggerFactory.getLogger(EclipseLinkLogger.class);

	/**
	 * ログ出力内容の最大桁数。
	 * Eclipse上でデバッグしているときに、あまりに長い文字列をログ出力しようとすると
	 * コンソールのバッファがあふれてロックされてしまう。
	 * これを回避するため、出力文字列の上限値を定めて、超過分をカットする。
	 */
	private static final int LOG_MAX_LENGTH = 1024 * 16;

	@Override
	public void log(SessionLogEntry sle) {
		// SQLの整形
		String msg = sle.getMessage();
		int len = msg == null ? 0 : msg.length();
		if (0 < len) {
			// Eclipse上でデバッグしているときに、あまりに長い文字列をログ出力しようとすると
			// コンソールのバッファがあふれてロックされてしまう。
			// これを回避するため、出力文字列の上限値を定めて、超過分をカットする。
			if (LOG_MAX_LENGTH < len) {
				msg = msg.substring(0, LOG_MAX_LENGTH) + "……";
			}
			msg = msg.replaceAll("[\r\n\t ]+", " ");

			log.debug(msg);
		}
	}
}
