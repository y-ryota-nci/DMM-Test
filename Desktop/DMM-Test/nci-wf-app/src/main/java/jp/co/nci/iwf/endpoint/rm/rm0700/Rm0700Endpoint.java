package jp.co.nci.iwf.endpoint.rm.rm0700;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 参加者ロール一覧Endpoint【企業管理者】
 */
@Endpoint
@Path("/rm0700")
@RequiredLogin
@WriteAccessLog
public class Rm0700Endpoint extends BaseEndpoint<Rm0700Request> {

	@Inject
	private Rm0700Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Rm0700Response init(Rm0700Request req) {
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
	public Rm0700Response search(Rm0700Request req) {
		return service.search(req);
	}

	/**
	 * 追加
	 * @param req
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rm0700Response add(Rm0700Request req) {
		return service.add(req);
	}

	/**
	 * 参加者ロールをZIPファイルとしてダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/downloadZip")
	public Response downloadZip(Rm0700Request req) {
		final String menuRoleType = MenuRoleType.CORPORATION;
		final String fileName = "MENUROLE-" + req.corporationCode + "@" + menuRoleType + ".zip";
		return DownloadUtils.download(fileName, service.downloadZip(req.corporationCode, menuRoleType, req.menuRoleCodes));
	}
}
