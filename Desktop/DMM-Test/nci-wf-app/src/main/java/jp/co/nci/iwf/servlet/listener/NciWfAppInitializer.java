package jp.co.nci.iwf.servlet.listener;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.component.system.SqlService;

/**
 * APP全体での初期化クラス
 */
public class NciWfAppInitializer implements ServletContextListener {
	private static Logger log = LoggerFactory.getLogger(NciWfAppInitializer.class);
	private static ServletContext ctx;

	public static ServletContext get() {
		return ctx;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ctx = sce.getServletContext();

		log.info("ServletContextHolder.contextInitialized START");

		// 初回アクセスを高速化するため、あらかじめリソースを読み込んでおく
		final CDI<Object> cdi = CDI.current();
		final Collection<String> localeCodes = LocaleService.INIT_LOCALE_CODES;
		final String langs = Arrays.toString(localeCodes.toArray());

		// 多言語メッセージ
		log.info("=================================");
		log.info("init i18nService for {}", langs);
		final I18nService i18Service = cdi.select(I18nService.class).get();
		i18Service.loadAndCache(localeCodes);

		// SQL
		log.info("=================================");
		log.info("init SqlService");
		final SqlService sqlService = cdi.select(SqlService.class).get();
		sqlService.loadAndCache();

		// DB接続情報
		final DestinationDatabaseService destination = cdi.select(DestinationDatabaseService.class).get();
		log.info("=================================");
		log.info("DataSource.Name : {}", destination.getDatabaseName());
		log.info("DataSource.Driver : {}", destination.getDriver());
		log.info("DataSource.URL : {}", destination.getUrl());
		log.info("DataSource.user : {}", destination.getUser());

		// META-INF/MANIFEST.MF
		final ManifestService manifest = cdi.select(ManifestService.class).get();
		log.info("=================================");
		log.info("Project-Name : {}", manifest.getName());
		log.info("Build Version : {}",  manifest.getVersion());
		log.info("Project-GroupId : {}", manifest.getGroupId());
		log.info("Project-ArtifactId : {}", manifest.getArtifactId());
		log.info("Build Timestamp : {}", manifest.getBuildTimestamp());
		log.info("Build Jdk : {}", manifest.getBuildJdk());
		log.info("Build by : {}", manifest.getBuildBy());

		log.info("ServletContextHolder.contextInitialized END");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ctx = null;
	}
}
