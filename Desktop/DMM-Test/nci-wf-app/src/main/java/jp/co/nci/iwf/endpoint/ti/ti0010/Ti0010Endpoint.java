package jp.co.nci.iwf.endpoint.ti.ti0010;

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
 * マスタ取込設定画面Endpoint
 */
@Path("/ti0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ti0010Endpoint extends BaseEndpoint<Ti0010Request> {
	@Inject private Ti0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ti0010Response init(Ti0010Request req) {
		return service.init(req);
	}

	/**
	 * テーブルカテゴリID配下の取込済みテーブルを再読み込み
	 */
	@POST
	@Path("/refresh")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ti0010Response refresh(Ti0010Request req) {
		return service.refresh(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ti0010Response save(Ti0010SaveRequest req) {
		return service.save(req);
	}
}
