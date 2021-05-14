package jp.co.nci.iwf.endpoint.mm.mm0430;

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
 * 役職一覧Endpoint
 */
@Path("/mm0430")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0430Endpoint extends BaseEndpoint<Mm0430Request> {

	@Inject
	private Mm0430Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0430Response init(Mm0430Request req) {
		return service.init(req);
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0430Response search(Mm0430Request req) {
		return service.search(req);		// 基本、ポップアップ版の役職選択画面と同一動作だ
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0430Response add(Mm0430Request req) {
		return service.add(req);
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
	public Mm0430Response delete(Mm0430Request req) {
		return service.delete(req);
	}
}
