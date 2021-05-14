package jp.co.nci.iwf.endpoint.dc.dc0111;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面文書定義設定のEndpoint
 */
@Path("/dc0111")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0111Endpoint extends BaseEndpoint<Dc0111InitRequest> {
	@Inject Dc0111Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0111InitResponse init(Dc0111InitRequest req) {
		return service.init(req);
	}

	/**
	 * 画面変更時
	 */
	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0111ChangeResponse change(Dc0111ChangeRequest req) {
		return service.change(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Dc0111SaveRequest req) {
		return service.save(req);
	}

}
