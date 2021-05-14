package jp.co.nci.iwf.endpoint.up.up0020;

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
 * プロセス定義アップロード画面Endpoint
 */
@Path("/up0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0020Endpoint extends BaseEndpoint<Up0020Request> {
	@Inject private Up0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0020Response init(Up0020Request req) {
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
	public Up0020Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * プロセス定義の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0020Response register(Up0020Request req) {
		return service.register(req);
	}

	/**
	 * プロセス定義の上書き登録確認
	 */
	@POST
	@Path("/confirm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0020Response confirm(Up0020Request req) {
		return service.confirm(req);
	}
}
