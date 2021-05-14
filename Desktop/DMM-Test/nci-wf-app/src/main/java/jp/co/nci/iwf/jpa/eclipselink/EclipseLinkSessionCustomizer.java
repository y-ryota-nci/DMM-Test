package jp.co.nci.iwf.jpa.eclipselink;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;

/**
 * EclipseLinkの動作をカスタマイズ
 */
public class EclipseLinkSessionCustomizer implements SessionCustomizer {

	@Override
	public void customize(Session session) throws Exception {
		// ログ出力先を委譲
        final SessionLog logger = new EclipseLinkLogger();
        logger.setLevel(SessionLog.FINE); //ここはpersistence.xmlのログレベルにしたいが…
		session.setSessionLog(logger);

		final DatabaseLogin dl = session.getLogin();
		dl.setShouldTrimStrings(true);
	}
}
