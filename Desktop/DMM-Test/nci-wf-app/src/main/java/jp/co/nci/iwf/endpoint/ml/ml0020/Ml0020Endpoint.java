package jp.co.nci.iwf.endpoint.ml.ml0020;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メール環境設定Endpoint
 */
@Path("/ml0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ml0020Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject private Ml0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ml0020InitResponse init(BaseRequest req) {
		return service.init(req);
	}


	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ml0020SaveResponse save(Ml0020SaveRequest req) {
		return service.save(req);
	}
}
