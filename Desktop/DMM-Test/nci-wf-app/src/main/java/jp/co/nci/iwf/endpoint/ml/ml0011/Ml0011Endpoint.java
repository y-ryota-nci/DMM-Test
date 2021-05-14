package jp.co.nci.iwf.endpoint.ml.ml0011;

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
 * メールテンプレート編集Endpoint
 */
@Path("/ml0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ml0011Endpoint extends BaseEndpoint<Ml0011InitRequest> {
	@Inject private Ml0011Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ml0011InitResponse init(Ml0011InitRequest req) {
		return service.init(req);
	}

	/**
	 * 保存
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ml0011SaveResponse save(Ml0011SaveRequest req) {
		return service.save(req);
	}

	/**
	 * プレビュー
	 */
	@POST
	@Path("/preview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ml0011PreviewResponse preview(Ml0011PreviewRequest req) {
		return service.preview(req);
	}
}
