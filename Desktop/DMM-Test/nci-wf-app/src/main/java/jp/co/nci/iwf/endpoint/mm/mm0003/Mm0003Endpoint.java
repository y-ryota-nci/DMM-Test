package jp.co.nci.iwf.endpoint.mm.mm0003;

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
 * 【プロファイル管理】ユーザ編集画面Endpoint
 */
@Path("/mm0003")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0003Endpoint extends BaseEndpoint<Mm0003InitRequest> {
	@Inject
	private Mm0003Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0003InitResponse init(Mm0003InitRequest req) {
		return service.init(req);
	}

	/**
	 * ユーザ情報の更新（ユーザ更新＋ユーザ所属の追加／削除）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0003UpdateUserRequest req) {
		return service.update(req);
	}

	/**
	 * ユーザ情報の削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Mm0003UpdateUserRequest req) {
		return service.delete(req);
	}
}
