package jp.co.nci.iwf.endpoint.wl.wl0010;

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
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * トレイ設定一覧（管理者）Endpoint
 */
@Path("/wl0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0010Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Wl0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
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
	public Wl0010Response search(Wl0010Request req) {
		return service.search(req);
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
	public Wl0010Response delete(Wl0010Request req) {
		return service.delete(req);
	}

	/**
	 * トレイ設定をZIPファイルとしてダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/downloadZip/{corporationCode}")
	public Response downloadZip(@PathParam("corporationCode") String corporationCode ) {
		String yyyyMMdd = toStr(today(), "yyyyMMdd");
		String fileName = "TRAYCONFIG-" + corporationCode + "_" + yyyyMMdd + ".zip";
		return DownloadUtils.download(fileName, service.downloadZip(corporationCode));
	}
}
