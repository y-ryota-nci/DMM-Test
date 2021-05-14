package jp.co.nci.iwf.component.system;

import java.util.concurrent.Callable;

import javax.enterprise.inject.spi.CDI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLファイル読込スレッド用タスク。SQLサービスの内部で非同期実行される
 */
public class SqlReaderTask implements Callable<YamlMap> {
	private String path;
	/** ロガー */
	private Logger log = LoggerFactory.getLogger(getClass());

	public SqlReaderTask(String path) {
		this.path = path;
	}

	@Override
	public YamlMap call() throws Exception {
		log.debug("reading SQL file -> {}", path);
		final YamlService yaml = CDI.current().select(YamlService.class).get();
		return yaml.read(path);
	}
}
