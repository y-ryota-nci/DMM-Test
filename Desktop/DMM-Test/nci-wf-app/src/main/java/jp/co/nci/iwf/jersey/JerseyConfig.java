package jp.co.nci.iwf.jersey;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.CsrfProtectionFilter;

import jp.co.nci.iwf.jersey.exception.jsonExceptionMapper.JsonMappingExceptionMapper;
import jp.co.nci.iwf.jersey.exception.jsonExceptionMapper.JsonParseExceptionMapper;
import jp.co.nci.iwf.jersey.provider.JacksonConfig;

@ApplicationPath("endpoint")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		packages(true, "jp.co");

		// Jersey標準のMOXyでJSON変換を行う
		// アノテーション「@XmlRootElement」がないと変換されないので注意
//		register(MOXyConfig.class);
//		register(MoxyJsonFeature.class);

		// JacksonでJSON変換を行う
		register(JsonMappingExceptionMapper.class, 1);	// jackson標準のJsonMappingExceptionMapperを上書き
		register(JsonParseExceptionMapper.class, 1);	// jackson標準のJsonParseExceptionMapperを上書き
		register(JacksonConfig.class);
		register(JacksonFeature.class);

		// Beanにアノテーション「@XmlRootElement」がなくてもJSON変換を行う
		property("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);

		// CSRF対策フィルタ
		register(CsrfProtectionFilter.class);

		// Jerseyでmultipartリクエストを受け取れるように。
		register(MultiPartFeature.class);
	}
}
