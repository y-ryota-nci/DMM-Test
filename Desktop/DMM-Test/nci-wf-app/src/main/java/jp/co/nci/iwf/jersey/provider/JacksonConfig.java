package jp.co.nci.iwf.jersey.provider;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import jp.co.nci.iwf.jersey.provider.jackson.SqlDateDeserializer;
import jp.co.nci.iwf.jersey.provider.jackson.SqlDateSerializer;
import jp.co.nci.iwf.jersey.provider.jackson.TimestampDeserializer;
import jp.co.nci.iwf.jersey.provider.jackson.TimestampSerializer;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * Jacksonで、ObjectをJSON化する際の設定
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
//@ApplicationScoped
public class JacksonConfig implements ContextResolver<ObjectMapper> {
	/** ObjectMapper */
	private final static ObjectMapper mapper;

	/** デフォルトコンストラクタ. */
	static {
		mapper = new ObjectMapper();

		// NULL値を空文字として出力
//		DefaultSerializerProvider.Impl dsp = new DefaultSerializerProvider.Impl();
//		dsp.setNullValueSerializer(new NullValueSerializer());
//		mapper.setSerializerProvider(dsp);

		// 空Beanをエラーとしない
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// 日付
		mapper.setDateFormat(new SimpleDateFormat(MiscUtils.FORMAT_DATE));

		// java.sql.Date用の日付書式
		final SimpleModule m = new SimpleModule();
		m.addSerializer(new SqlDateSerializer());
		m.addDeserializer(java.sql.Date.class, new SqlDateDeserializer());
		// java.sql.Timestamp用の日付書式
		m.addSerializer(new TimestampSerializer());
		m.addDeserializer(java.sql.Timestamp.class, new TimestampDeserializer());
		mapper.registerModule(m);
		// java.text.BigDecimalの書式
		// →他でNumberFormatExceptionとなど副作用が大きいので廃止
//		m.addSerializer(new BigDecimalSerializer());
//		m.addDeserializer(BigDecimal.class, new BigDecimalDesirializer());

		// json出力をインデントして整形
		mapper.disable(SerializationFeature.INDENT_OUTPUT);

		// JAXB互換設定
//		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
//		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}
}
