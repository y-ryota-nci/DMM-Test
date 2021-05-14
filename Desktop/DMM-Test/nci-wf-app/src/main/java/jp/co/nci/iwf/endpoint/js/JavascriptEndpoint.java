package jp.co.nci.iwf.endpoint.js;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;

/**
 * 外部Javascript/静的Javascript用Endpoint
 */
@Path("/javascript")
@Endpoint
public class JavascriptEndpoint implements CodeBook, CodeBook.HTTP {
	@Inject private JavascriptService service;

	/**
	 * 静的Javascriptを返す
	 * @param fileName
	 * @param lastModified
	 * @return
	 */
	@GET
	@Path("/static/{fileName}")
	public Response getStaticJavascript(@PathParam("fileName") String fileName,
			@HeaderParam(IF_NONE_MATCH) String ifNoneMatch,
			@HeaderParam(IF_MODIFIED_SINCE) Date ifModifiedSince) {
		return service.responseStaticJavascript(
				fileName,
				ifNoneMatch,
				(ifModifiedSince == null ? null : ifModifiedSince.getTime()));
	}

	/**
	 * 外部Javascriptを返す
	 * @param javascriptIds
	 * @param ifModifiedSince
	 * @return
	 */
	@GET
	@Path("/outside")
	public Response getOutsideJavascript(@QueryParam("jsid") List<Long> javascriptIds,
			@HeaderParam(IF_NONE_MATCH) String ifNoneMatch,
			@HeaderParam(IF_MODIFIED_SINCE) Date ifModifiedSince) {
		return service.responseOutsideJavascript(
				javascriptIds,
				ifNoneMatch,
				(ifModifiedSince == null ? null : ifModifiedSince.getTime()));
	}
}
