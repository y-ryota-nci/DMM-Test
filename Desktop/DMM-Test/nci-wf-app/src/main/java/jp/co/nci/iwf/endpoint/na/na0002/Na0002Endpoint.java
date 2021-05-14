package jp.co.nci.iwf.endpoint.na.na0002;

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

/**
 * メニュー業務管理項目名称設定Endpoint
 */
@Path("/na0002")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Na0002Endpoint extends BaseEndpoint<Na0002Request> {
	@Inject
	private Na0002Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Na0002Response init(Na0002Request req) {
		return service.init(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0002Response create(Na0002Request req) {
		return service.create(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0002Response update(Na0002Request req) {
		return service.update(req);
	}

	/**
	 * 削除
	 */
	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Na0002Response remove(Na0002Request req) {
		return service.remove(req);
	}

}
