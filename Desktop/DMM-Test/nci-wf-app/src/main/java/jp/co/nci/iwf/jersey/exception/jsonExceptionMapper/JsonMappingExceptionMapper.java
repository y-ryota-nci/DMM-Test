package jp.co.nci.iwf.jersey.exception.jsonExceptionMapper;

import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * JSONとBeanの紐付けるに起因した例外マッパー
 */
@Provider
public class JsonMappingExceptionMapper
	extends BaseJsonExceptionMapper<JsonMappingException> {

}
