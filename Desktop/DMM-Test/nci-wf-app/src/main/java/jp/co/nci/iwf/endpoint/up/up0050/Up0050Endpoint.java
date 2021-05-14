package jp.co.nci.iwf.endpoint.up.up0050;

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
 * メールテンプレート定義アップロード画面Endpoint
 */
@Path("/up0050")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0050Endpoint extends BaseEndpoint<Up0050Request> {
	@Inject private Up0050Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0050Response init(Up0050Request req) {
		return service.init(req);
	}

	/**
	 * メールテンプレート定義のファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0050Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * メールテンプレート定義の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0050Response register(Up0050Request req) {
		return service.register(req);
	}
}
