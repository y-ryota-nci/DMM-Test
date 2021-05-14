package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import jp.co.nci.iwf.util.MiscUtils;

/** java.sql.Date用のシリアライザ */
public class SqlDateSerializer extends StdSerializer<java.sql.Date> {
	public SqlDateSerializer() {
		super(java.sql.Date.class);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value == null)
			gen.writeString("");
		else
			gen.writeString(MiscUtils.FORMATTER_DATE.get().format(value));
	}
}