package jp.co.nci.iwf.jersey.exception.jsonExceptionMapper;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import jp.co.nci.iwf.component.accesslog.AccessLogService;

/**
 * リクエスト／レスポンスとJSONのマッピングがうまくいかない時の例外マッパー基底クラス
 */
public abstract class BaseJsonExceptionMapper<E extends JsonProcessingException>
	implements ExtendedExceptionMapper<E> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public Response toResponse(E e) {
		// 例外内容をアクセスログへ記録
		try {
			AccessLogService accessLog = CDI.current().select(AccessLogService.class).get();
			accessLog.appendException(e);
		} catch (Exception ex) {
			log.warn(ex.getMessage(), ex);
		}

		log.error(e.getMessage(), e);

	    return Response.status(Response.Status.BAD_REQUEST)
	    		.entity(e.getMessage())
	    		.type("text/plain; charset=UTF-8").build();
	}

	public boolean isMappable(E e) {
		return JsonProcessingException.class.isAssignableFrom(e.getClass());
	}
}
