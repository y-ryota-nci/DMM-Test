package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/** NULL値を空文字にするシリアライザー */
public class NullValueSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object t, JsonGenerator jg, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jg.writeString("");
	}
}