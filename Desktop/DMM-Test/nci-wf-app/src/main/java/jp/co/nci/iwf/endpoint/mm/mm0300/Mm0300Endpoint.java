package jp.co.nci.iwf.endpoint.mm.mm0300;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * ルート一覧Endpoint
 */
@Path("/mm0300")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0300Endpoint extends BaseEndpoint<Mm0300Request>{

	@Inject
	private Mm0300Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(Mm0300Request req) {
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
	public BaseResponse search(Mm0300Request req) {
		return service.search(req);
	}


	/**
	 * コンテナとその配下テーブルに対して、コンテナIDに該当する全レコードを削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/downloadZip")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadZip(Mm0300Request req) {
		final String yyyyMMdd = toStr(now(), "yyyyMMdd");
		String fileName = null;

		if (req.procList == null || req.procList.isEmpty())
			throw new BadRequestException("プロセス定義リストが未指定です");
		if (req.procList.size() == 1) {
			String processDefCode = req.procList.get(0).getProcessDefCode();
			String processDefDetailCode = req.procList.get(0).getProcessDefDetailCode();
			fileName = "PROCESS_" + processDefCode + "-" + processDefDetailCode + ".zip";
		} else {
			fileName = "PROCESS_" + yyyyMMdd + ".zip";
		}
		return DownloadUtils.download(fileName,
				service.downloadZip(req.procList));
	}

	/**
	 * 枝番更新
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateBranch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mm0300BranchResponse updateBranch(Mm0300BranchRequest req) {
		return service.updateBranch(req);
	}

}
