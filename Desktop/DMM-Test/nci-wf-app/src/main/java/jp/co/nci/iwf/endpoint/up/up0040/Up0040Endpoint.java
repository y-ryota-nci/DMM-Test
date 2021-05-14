package jp.co.nci.iwf.endpoint.up.up0040;

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
 * トレイ表示設定アップロード画面Endpoint
 */
@Path("/up0040")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0040Endpoint extends BaseEndpoint<Up0040Request> {
	@Inject private Up0040Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Up0040Response init(Up0040Request req) {
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
	public Up0040Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * トレイ表示設定の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Up0040Response register(Up0040Request req) {
		return service.register(req);
	}
}
