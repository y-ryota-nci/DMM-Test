package jp.co.nci.iwf.endpoint.vd.vd0041;

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
 * 画面プロセス定義設定のEndpoint
 */
@Path("/vd0041")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0041Endpoint extends BaseEndpoint<Vd0041InitRequest> {
	@Inject Vd0041Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0041InitResponse init(Vd0041InitRequest req) {
		return service.init(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Vd0041SaveRequest req) {
		return service.save(req);
	}

	/**
	 * プロセス定義コードに対するプロセス定義明細コードの選択肢を抽出
	 */
	@POST
	@Path("/getProcessDefDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0041DetailResponse getProcessDefDetails(Vd0041DetailRequest req) {
		return service.getProcessDefDetails(req);
	}
}
