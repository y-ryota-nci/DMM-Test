package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * Timestamp型のデシリアライザ
 */
public class TimestampDeserializer extends StdDeserializer<Timestamp> {
	public TimestampDeserializer() {
		super(Timestamp.class);
	}

	@Override
	public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			String value = p.getValueAsString();
			if (StringUtils.isNotEmpty(value)) {
				// 年月日時分秒までを普通に日付変換し、端数のミリ秒／ナノ秒以下をTimestampへ直渡し
				final String[] values = value.split("\\.");
				if (values.length > 0) {
					final Date d = MiscUtils.FORMATTER_DATETIME.get().parse(values[0]);
					final Timestamp tm = new Timestamp(d.getTime());
					if (values.length > 1) {
						final Integer nano = Integer.valueOf(StringUtils.rightPad(values[1], 9, '0') );
						tm.setNanos(nano);
					}
					return tm;
				}
			}
			return null;
		}
		catch (ParseException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
