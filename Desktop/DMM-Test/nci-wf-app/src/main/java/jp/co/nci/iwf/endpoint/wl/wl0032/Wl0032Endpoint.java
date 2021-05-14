package jp.co.nci.iwf.endpoint.wl.wl0032;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 自案件検索画面Endpoint
 */
@Path("/wl0032")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0032Endpoint extends BaseEndpoint<BaseRequest> {

	@Inject private Wl0032Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public TrayInitResponse init(BaseRequest req) {
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
	public BaseTrayResponse search(TraySearchRequest req) {
		return service.search(req);
	}

	/**
	 * 検索(ガジェット：承認済み)
	 * @param req
	 * @return
	 */
	@POST
	@Path("/searchFromGadgetApproved")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseTrayResponse searchFromGadgetApproved(TraySearchRequest req) {
		return service.searchFromGadgetApproved(req);
	}

	/**
	 * CSVダウンロード
	 */
	@POST
	@Path("/downloadCsv")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadCsv(TraySearchRequest req) {
		String fileName = "wl0032.csv";
		return DownloadUtils.download(fileName, service.downloadCsv(req));
	}
}
