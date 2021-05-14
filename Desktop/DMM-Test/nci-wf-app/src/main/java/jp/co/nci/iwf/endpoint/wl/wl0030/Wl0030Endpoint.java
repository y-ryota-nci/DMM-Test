package jp.co.nci.iwf.endpoint.wl.wl0030;

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
 * ワークリストEndpoint
 */
@Path("/wl0030")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Wl0030Endpoint extends BaseEndpoint<BaseRequest>{
	@Inject
	private Wl0030Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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
	 * 検索(ガジェット：申請待ち)
	 * @param req
	 * @return
	 */
	@POST
	@Path("/searchFromGadgetApplication")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseTrayResponse searchFromGadgetApplication(TraySearchRequest req) {
		return service.searchFromGadgetApplication(req);
	}

	/**
	 * 検索(ガジェット：承認待ち)
	 * @param req
	 * @return
	 */
	@POST
	@Path("/searchFromGadgetApproval")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseTrayResponse searchFromGadgetApproval(TraySearchRequest req) {
		return service.searchFromGadgetApproval(req);
	}

	/**
	 * CSVダウンロード
	 */
	@POST
	@Path("/downloadCsv")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadCsv(TraySearchRequest req) {
		String fileName = "wl0030.csv";
		return DownloadUtils.download(fileName, service.downloadCsv(req));
	}
}
