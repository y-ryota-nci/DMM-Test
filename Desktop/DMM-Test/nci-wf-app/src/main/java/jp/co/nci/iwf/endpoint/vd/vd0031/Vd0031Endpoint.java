package jp.co.nci.iwf.endpoint.vd.vd0031;

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
 * 画面設定Endpoint
 */
@Path("/vd0031")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0031Endpoint extends BaseEndpoint<Vd0031InitRequest> {
	@Inject Vd0031Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Vd0031InitResponse init(Vd0031InitRequest req) {
		return service.init(req);
	}

	/**
	 * 更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Vd0031SaveRequest req) {
		return service.save(req);
	}

	/**
	 * 表示条件リスト取得
	 */
	@POST
	@Path("/getDcList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0031DcResponse getDcList(Vd0031ContainerRequest req) {
		return service.getDcList(req);
	}

	/**
	 * プレビュー前処理
	 */
	@POST
	@Path("/preparePreview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0031PreviewResponse preparePreview(Vd0031ContainerRequest req) {
		return service.preparePreview(req);
	}
}
