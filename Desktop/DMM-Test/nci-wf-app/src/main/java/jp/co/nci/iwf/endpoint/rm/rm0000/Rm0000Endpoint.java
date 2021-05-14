package jp.co.nci.iwf.endpoint.rm.rm0000;

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
 * 参加者ロール一覧Endpoint【一般ユーザ】
 */
@Endpoint
@Path("/rm0000")
@RequiredLogin
@WriteAccessLog
public class Rm0000Endpoint extends BaseEndpoint<Rm0000Request> {

	@Inject
	private Rm0000Service service;

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
	public Rm0000Response init(Rm0000Request req) {
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
	public Rm0000Response search(Rm0000Request req) {
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
	public Rm0000Response add(Rm0000Request req) {
		return service.add(req);
	}

	/**
	 * 参加者ロールをZIPファイルとしてダウンロード
	 * @param req
	 * @return
	 */
	@POST
	@Path("/downloadZip")
	public Response downloadZip(Rm0000Request req) {
		String yyyyMMdd = toStr(today(), "yyyyMMdd");
		final String menuRoleType = MenuRoleType.NORMAL;
		final String fileName = "MENUROLE-" + req.corporationCode + "@" + menuRoleType + "_" + yyyyMMdd + ".zip";
		return DownloadUtils.download(fileName, service.downloadZip(req.corporationCode, menuRoleType, req.menuRoleCodes));
	}
}
