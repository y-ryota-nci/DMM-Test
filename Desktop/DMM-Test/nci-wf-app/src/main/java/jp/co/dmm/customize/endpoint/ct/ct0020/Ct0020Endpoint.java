package jp.co.dmm.customize.endpoint.ct.ct0020;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jp.co.dmm.customize.component.catalog.CatalogService;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * カタログ選択画面Endpoint
 */
@Path("/ct0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ct0020Endpoint extends BaseEndpoint<Ct0020InitRequest> {

	@Inject private Ct0020Service service;
	@Inject private CatalogService catalogService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0020InitResponse init(Ct0020InitRequest req) {
		return service.init(req);
	}


	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0020SearchResponse getWorklist(Ct0020SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 画像
	 * @param catalogImageId
	 */
	@GET
	@Path("/download/catalogImage")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadPartsAttachFile(@QueryParam("catalogImageId") Long catalogImageId, @Context HttpServletRequest req) {
		// パーツデザイン時にハイパーリンクパーツで添付したファイル
		// プロセスIDに依存しない
		MwmCatalogImage entity = catalogService.getCatalogImage(catalogImageId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

}
