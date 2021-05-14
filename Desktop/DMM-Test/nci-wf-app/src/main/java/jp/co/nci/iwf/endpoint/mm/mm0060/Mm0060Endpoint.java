package jp.co.nci.iwf.endpoint.mm.mm0060;

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
 * ブロック表示順設定Endpoint
 */
@Path("/mm0060")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0060Endpoint extends BaseEndpoint<Mm0060InitRequest> {
	@Inject
	private Mm0060Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mm0060InitResponse init(Mm0060InitRequest req) {
		return service.init(req);
	}

	/**
	 * 検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0060SearchResponse search(Mm0060SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 登録
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0060SaveResponse save(Mm0060SaveRequest req) {
		return service.save(req);
	}

	/**
	 * デフォルトをコピー
	 */
	@POST
	@Path("/copyDefault")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0060SaveResponse copyDefault(Mm0060SaveRequest req) {
		return service.copyDefault(req);
	}

	/**
	 * 削除
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0060SaveResponse delete(Mm0060SaveRequest req) {
		return service.delete(req);
	}
}