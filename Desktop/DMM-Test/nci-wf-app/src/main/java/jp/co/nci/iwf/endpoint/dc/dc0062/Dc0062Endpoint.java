package jp.co.nci.iwf.endpoint.dc.dc0062;

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
 * 拡張項目登録Endpoint.
 */
@Endpoint
@Path("/dc0062")
@RequiredLogin
@WriteAccessLog
public class Dc0062Endpoint extends BaseEndpoint<Dc0062Request> {

	@Inject Dc0062Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0062Response init(Dc0062Request req) {
		return service.init(req);
	}

	/**
	 * 選択肢プロパティ変更時のデフォルト値変更処理
	 * @param req
	 * @return
	 */
	@GET
	@Path("/changeOption")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> changeOption(@QueryParam("corporationCode") String corporationCode, @QueryParam("optionId") Long optionId) {
		return service.changeOption(corporationCode, optionId);
	}

	/**
	 * 登録・更新
	 */
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0062Response save(Dc0062SaveRequest req) {
		return service.save(req);
	}
}
