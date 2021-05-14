package jp.co.nci.iwf.endpoint.vd.vd0110;

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
 * 画面コンテナ設定Endpoint
 */
@Path("/vd0110")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0110Endpoint extends BaseEndpoint<Vd0110InitRequest> {
	@Inject
	private Vd0110Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Vd0110InitRequest req) {
		return service.init(req);
	}

	/**
	 * パーツ追加
	 * @param req
	 * @return
	 */
	@POST
	@Path("/addParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse addParts(Vd0110PartsRequest req) {
		return service.addParts(req);
	}

	/**
	 * パーツ編集
	 * @param req
	 * @return
	 */
	@POST
	@Path("/editParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse editParts(Vd0110PartsRequest req) {
		return service.editParts(req);
	}

	/**
	 * パーツコピー
	 * @param req
	 * @return
	 */
	@POST
	@Path("/copyParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse copyParts(Vd0110PartsRequest req) {
		return service.copyParts(req);
	}

	/**
	 * パーツ削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/deleteParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse deleteParts(Vd0110PartsRequest req) {
		return service.deleteParts(req);
	}

	/**
	 * パーツコピー
	 * @param req
	 * @return
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Vd0110SaveRequest req) {
		return service.save(req);
	}

	/**
	 * 背景HTML変更
	 * @param req
	 * @return
	 */
	@POST
	@Path("/editBgHtml")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse editBgHtml(Vd0110PartsRequest req) {
		return service.editBgHtml(req);
	}

	/**
	 * 再描画
	 * @param req
	 * @return
	 */
	@POST
	@Path("/refresh")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0110PartsResponse refresh(Vd0110PartsRequest req) {
		return service.refresh(req);
	}
}
