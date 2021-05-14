package jp.co.nci.iwf.endpoint.mm.mm0100;

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
 * 情報共有設定Endpoint
 */
@Path("/mm0100")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0100Endpoint extends BaseEndpoint<Mm0100InitRequest>{

	@Inject
	private Mm0100Service service;

	/**
	 * 初期化処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0100InitResponse init(Mm0100InitRequest req) {
		return service.init(req);
	}

	/**
	 * 検索処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0100SearchResponse search(Mm0100SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0100SaveResponse update(Mm0100SaveRequest req) {
		return service.update(req);
	}

	/**
	 * 削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0100SaveResponse remove(Mm0100SaveRequest req) {
		return service.remove(req);
	}

}
