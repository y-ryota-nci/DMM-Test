package jp.co.nci.iwf.jersey.exception.jsonExceptionMapper;

import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * JSONを解析する際の例外マッパー
 */
@Provider
public class JsonParseExceptionMapper
		extends BaseJsonExceptionMapper<JsonParseException> {

}
