package jp.co.nci.iwf.endpoint.ti.ti0011;

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
 * カテゴリ編集画面Endpoint
 */
@Path("/ti0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0011Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Ti0011Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0011Response init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ti0011Response save(Ti0011SaveRequest req) {
		return service.save(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ti0011Response delete(Ti0011SaveRequest req) {
		return service.delete(req);
	}
}
