package jp.co.nci.iwf.endpoint.dc.dc0906;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 文書ファイル情報コピー先情報入力画面Endpoint.
 */
@Path("/dc0906")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0906Endpoint extends BaseEndpoint<Dc0906Request> {


	@Inject private Dc0906Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0906Response init(Dc0906Request req) {
		return service.init(req);
	}

	/**
	 * 文書情報検索処理
	 * @param req
	 * @return
	 */
	@GET
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> search(@QueryParam("title") String title, @QueryParam("excludeDocId") Long excludeDocId) {
		return service.search(title, excludeDocId);
	}

}
