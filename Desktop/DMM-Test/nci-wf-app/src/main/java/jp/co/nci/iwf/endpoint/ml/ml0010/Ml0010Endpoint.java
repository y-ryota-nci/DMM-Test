package jp.co.nci.iwf.endpoint.ml.ml0010;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * メールテンプレート一覧Endpoint
 */
@Path("/ml0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ml0010Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Ml0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Ml0010InitResponse init(BaseRequest req) {
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
	public Ml0010Response search(Ml0010Request req) {
		return service.search(req);
	}

	/**
	 * メールテンプレート定義をZIPとしてダウンロード
	 */
	@POST
	@Path("/download")
	public Response download(@QueryParam("corporationCode") String corporationCode) {
		String yyyyMMdd = toStr(today(), "yyyyMMdd");
		String fileName = "MAILTEMPLATE-" + corporationCode + "_" + yyyyMMdd + ".zip";
		return DownloadUtils.download(fileName, service.download(corporationCode));
	}

	/**
	 * 削除
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Ml0010DeleteRequest req) {
		return service.delete(req);
	}
}
