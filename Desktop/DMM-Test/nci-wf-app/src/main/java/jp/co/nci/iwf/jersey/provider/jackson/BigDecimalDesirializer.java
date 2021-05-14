package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * BigDecimalのデシリアラザ
 */
public class BigDecimalDesirializer extends StdDeserializer<BigDecimal> {
	public BigDecimalDesirializer() {
		super(BigDecimal.class);
	}

	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		if (p.getValueAsString() == null)
			return null;

		try {
			Number value = NumberFormat.getNumberInstance().parse(p.getValueAsString());
			if (value instanceof BigDecimal)
				return (BigDecimal)value;
			if (value instanceof Long)
				return new BigDecimal((Long)value);
			if (value instanceof Integer)
				return new BigDecimal((Integer)value);
			return new BigDecimal(value.toString());
		}
		catch (ParseException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
