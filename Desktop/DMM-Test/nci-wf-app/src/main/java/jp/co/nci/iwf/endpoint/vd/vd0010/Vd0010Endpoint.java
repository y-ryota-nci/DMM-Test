package jp.co.nci.iwf.endpoint.vd.vd0010;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
 * コンテナ一覧Endpoint
 */
@Path("/vd0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0010Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject private Vd0010Service service;

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
	public Vd0010Response search(Vd0010Request req) {
		return service.search(req);
	}

	/**
	 * コンテナのカラム定義をDBへ反映
	 * @param req
	 * @return
	 */
	@POST
	@Path("/syncDB")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse syncDB(Vd0010ContainerRequest req) {
		return service.syncDB(req);
	}

	/**
	 * コンテナとその配下テーブルに対して、コンテナIDに該当する全レコードを削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Vd0010ContainerRequest req) {
		return service.delete(req);
	}


	/**
	 * コンテナ定義をEXCEL形式でダウンロード
	 */
	@POST
	@Path("/downloadExcel")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadExcel(Vd0010ContainerRequest req) {
		final Long containerId = req.containerIds.get(0);
		if (containerId == null)
			throw new BadRequestException("コンテナIDが未指定です");

		final Vd0010Entity container = service.get(containerId);
		if (container == null)
			throw new NotFoundException("コンテナ情報が見つかりません。containerId=" + containerId);

		final String fileName = container.containerCode + "_" + container.containerName + ".xlsx";
		return DownloadUtils.download(fileName, service.downloadExcel(containerId));
	}
}
