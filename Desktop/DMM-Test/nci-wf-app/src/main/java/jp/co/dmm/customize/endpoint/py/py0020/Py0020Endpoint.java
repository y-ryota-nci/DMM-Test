package jp.co.dmm.customize.endpoint.py.py0020;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 確認_支払実績一覧Endpoint
 */
@Path("/py0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Py0020Endpoint extends BaseEndpoint<BaseRequest>{
	@Inject
	private Py0020Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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
	public BaseResponse getWorklist(TraySearchRequest req) {
		return service.search(req);
	}

	/**
	 * 画面コード取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getScreenCode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse getScreenCode(Py0020RedirectRequest req) {
		return service.getScreenCode(req);
	}

	/**
	 * 処理実行
	 * @param req
	 * @return
	 */
	@POST
	@Path("/execute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse execute(Py0020ExecuteRequest req) {
		return service.execute(req);
	}

}
