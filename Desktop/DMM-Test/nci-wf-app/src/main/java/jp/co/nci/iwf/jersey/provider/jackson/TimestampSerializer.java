package jp.co.nci.iwf.jersey.provider.jackson;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * Timestamp型のシリアライザ
 */
public class TimestampSerializer extends StdSerializer<Timestamp> {

	public TimestampSerializer() {
		super(Timestamp.class);
	}

	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null)
			gen.writeString("");
		else {
			// Timestamp型はナノ秒まで保持しているので、それを維持しながらシリアライズ
			StringBuilder nanos = new StringBuilder().append(value.getNanos());
			if (value.getNanos() != 0) {
				// 先頭にゼロを付与
				while (nanos.length() < 9) {
					nanos.insert(0, '0');
				}
				// 末尾のゼロを除去
				for (int i = nanos.length() - 1; nanos.charAt(i) == '0'; i--) {
					nanos.deleteCharAt(i);
				}
			}

			String d = new StringBuilder(32)
					.append(MiscUtils.FORMATTER_DATETIME.get().format(value))
					.append(".")
					.append(nanos.toString())
					.toString();
			gen.writeString(d);
		}
	}
}