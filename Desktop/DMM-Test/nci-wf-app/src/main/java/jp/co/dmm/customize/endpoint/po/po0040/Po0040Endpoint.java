package jp.co.dmm.customize.endpoint.po.po0040;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * kintoneデータアップロード画面Endpoint
 */
@Path("/po0040")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Po0040Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Po0040Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Po0040InitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * kintoneデータアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(@FormDataParam("targetYm") String targetYm, FormDataMultiPart multiPart) {
		return service.upload(targetYm, multiPart);
	}
}
