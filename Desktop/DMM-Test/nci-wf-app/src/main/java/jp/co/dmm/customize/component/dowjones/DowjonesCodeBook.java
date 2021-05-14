package jp.co.dmm.customize.component.dowjones;

public interface DowjonesCodeBook {

	interface Format {
		String JSON = "json";
		String XML = "xml";
	}

	interface ParameterName {
		String KEY = "key";
	}

	interface API {
		/** 名前検索 */
		String SEARCH_NAME = "/search/course/extreme";
	}

	interface PROTOCOL {
		String HTTP = "http";
		String HTTPS = "https";
	}

	interface ProxyConfig {
		/** 使用　可・不可 */
		String USE = "USE";
		String PROTOCOL = "PROTOCOL";
		String HTTP_HOST = "HTTP_HOST";
		String HTTP_PORT = "HTTP_PORT";
		String HTTPS_HOST = "HTTPS_HOST";
		String HTTPS_PORT = "HTTPS_PORT";
		String USERNAME = "USERNAME";
		String PASSWORD = "PASSWORD";
	}

	interface DowjonesConfig {
		String USE = "USE";
		String PROTOCOL = "PROTOCOL";
		String WEBSERVER = "WEBSERVER";
		String VERSION = "VERSION";
		String USERNAME = "USERNAME";
		String PASSWORD = "PASSWORD";
		String NAMESPACE = "NAMESPACE";
	}

}
