package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * BigDecimal型のシリアライザ
 */
public class BigDecimalSerializer extends StdSerializer<BigDecimal>{
	public BigDecimalSerializer() {
		super(BigDecimal.class);
	}

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null)
			gen.writeString("");
		else
			gen.writeString(NumberFormat.getNumberInstance().format(value));
	}
}
