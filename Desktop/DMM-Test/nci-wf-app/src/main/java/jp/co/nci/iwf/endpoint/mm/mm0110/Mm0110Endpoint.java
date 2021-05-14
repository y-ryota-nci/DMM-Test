package jp.co.nci.iwf.endpoint.mm.mm0110;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * システムプロパティ編集画面Endpoint
 */
@Path("/mm0110")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0110Endpoint extends BaseEndpoint<Mm0110InitRequest> {

	@Inject
	private Mm0110Service service;

	/**
	 * 初期化処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0110InitResponse init(Mm0110InitRequest req) {
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
	public Mm0110SearchResponse search(Mm0110SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 保存処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0110SaveResponse save(Mm0110SaveRequest req) {
		return service.save(req);
	}

	/**
	 * 環境設定内容をZIPファイルとしてダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/download/{corporationCode}")
	public Response download(@PathParam("corporationCode") String corporationCode) {
		final String yyyyMMdd = toStr(today(), "yyyyMMdd");
		final String fileName = "SYSTEM_CONFIG_" + corporationCode + "_" + yyyyMMdd + ".zip";
		return DownloadUtils.download(fileName, service.download(corporationCode));
	}
}
