package jp.co.nci.iwf.jersey.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Moxyだとルート用のMapはJSON化できないので、JacksonでJSON化
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings("rawtypes")
//@ApplicationScoped
public class MessageBodyWriterMap implements MessageBodyWriter<Map> {
	private static final ObjectMapper om = JacksonConfig.getObjectMapper();

	public MessageBodyWriterMap() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (Map.class.isAssignableFrom(type));
	}

	@Override
	public long getSize(Map t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Map t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		final String json = om.writeValueAsString(t);
		PrintWriter out = new PrintWriter(entityStream);
		out.print(json);
		out.flush();
	}
}
