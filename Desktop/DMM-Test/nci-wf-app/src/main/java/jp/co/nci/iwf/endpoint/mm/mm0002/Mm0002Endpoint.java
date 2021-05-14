package jp.co.nci.iwf.endpoint.mm.mm0002;

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
 * 【プロファイル管理】組織編集画面Endpoint
 */
@Path("/mm0002")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0002Endpoint extends BaseEndpoint<Mm0002InitRequest> {
	@Inject
	private Mm0002Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Mm0002InitRequest req) {
		return service.init(req);
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0002UpdateResponse update(Mm0002UpdateRequest req) {
		return service.update(req);
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
	public Mm0002UpdateResponse delete(Mm0002UpdateRequest req) {
		return service.delete(req);
	}
}
