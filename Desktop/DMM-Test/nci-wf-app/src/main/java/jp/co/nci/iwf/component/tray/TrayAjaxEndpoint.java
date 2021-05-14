package jp.co.nci.iwf.component.tray;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.NotImplementedException;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * トレイ系画面のAjax用Endpoint
 */
@Path("/trayAjax")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class TrayAjaxEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject private TrayAjaxService service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TrayInitResponse init(BaseRequest req) {
		throw new NotImplementedException("決して使用されないはずのメソッド呼び出しです");
	}


	/**
	 * ユーザマスタ
	 * @return
	 */
	@POST
	@Path("/getWfmUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse getWfmUser(TrayNameRequest req) {
		return service.getWfmUser(req);
	}

	/**
	 * 組織マスタ
	 * @return
	 */
	@POST
	@Path("/getWfmOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse getWfmOrganization(TrayNameRequest req) {
		return service.getWfmOrganization(req);
	}

	/**
	 * 役職マスタ
	 * @return
	 */
	@POST
	@Path("/getWfmPost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse getWfmPost(TrayNameRequest req) {
		return service.getWfmPost(req);
	}

	/**
	 * 企業マスタ
	 * @return
	 */
	@POST
	@Path("/getWfmCorporation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BasePagingResponse getWfmCorporation(TrayNameRequest req) {
		return service.getWfmCorporation(req);
	}
}
