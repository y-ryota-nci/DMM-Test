package jp.co.nci.iwf.endpoint.up.up0010;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 画面定義アップロード画面Endpoint
 */
@Path("/up0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0010Endpoint extends BaseEndpoint<Up0010Request> {
	@Inject private Up0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0010Response init(Up0010Request req) {
		return service.init(req);
	}

	/**
	 * ファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0010Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * 画面定義の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0010Response register(Up0010Request req) {
		return service.register(req);
	}

	/**
	 * 画面定義の上書き登録確認
	 */
	@POST
	@Path("/confirm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0010Response confirm(Up0010Request req) {
		return service.confirm(req);
	}
}
