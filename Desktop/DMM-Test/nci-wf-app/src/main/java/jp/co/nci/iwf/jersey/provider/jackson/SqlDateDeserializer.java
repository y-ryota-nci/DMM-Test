package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import jp.co.nci.iwf.util.MiscUtils;

/** java.sql.Date用のデシリアライザ */
public class SqlDateDeserializer extends StdDeserializer<java.sql.Date> {
	public SqlDateDeserializer() {
		super(java.sql.Date.class);
	}

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			String value = p.getValueAsString();
			if (StringUtils.isEmpty(value))
				return null;
			java.util.Date d = MiscUtils.FORMATTER_DATE.get().parse(value);
			return new java.sql.Date(d.getTime());
		} catch (ParseException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
