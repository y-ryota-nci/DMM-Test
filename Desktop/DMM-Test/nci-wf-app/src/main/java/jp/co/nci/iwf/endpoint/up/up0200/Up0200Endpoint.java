package jp.co.nci.iwf.endpoint.up.up0200;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル情報アップロード画面Endpoint
 */
@Path("/up0200")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Up0200Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Up0200Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * プロファイル情報アップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * プロファイル情報の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(Up0200SaveRequest req) {
		return service.register(req);
	}

	/**
	 * テンプレートのダウンロード
	 */
	@POST
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response download() {
		return service.download();
	}
}
