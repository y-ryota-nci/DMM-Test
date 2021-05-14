package jp.co.nci.iwf.endpoint.up.up0030;

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
 * メニューロール定義アップロード画面Endpoint
 */
@Path("/up0030")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0030Endpoint extends BaseEndpoint<Up0030Request> {
	@Inject private Up0030Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0030Response init(Up0030Request req) {
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
	public Up0030Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * メニューロール定義の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0030Response register(Up0030Request req) {
		return service.register(req);
	}
}
