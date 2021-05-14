package jp.co.dmm.customize.endpoint.py.py0010;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * クレカ明細データ取込画面Endpoint
 */
@Path("/py0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Py0010Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Py0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Py0010InitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * クレカ明細データアップロード
	 * @param payYm
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse upload(@FormDataParam("payYm") String payYm, FormDataMultiPart multiPart) {
		return service.upload(payYm, multiPart);
	}

}
