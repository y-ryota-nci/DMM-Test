package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.util.List;

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

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.dmm.customize.component.catalog.CatalogService;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * カタログ登録画面Endpoint
 */
@Path("/ct0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Ct0011Endpoint extends BaseEndpoint<Ct0011InitRequest> {

	@Inject private Ct0011Service service;
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
	public Ct0011InitResponse init(Ct0011InitRequest req) {
		return service.init(req);
	}

	/**
	 * ファイルアップロード
	 * @param bodyParts
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public CatalogImage upload(@FormDataParam("file") List<FormDataBodyPart> bodyParts) {
		return service.upload(bodyParts);
	}

	/**
	 * 画像
	 * @param catalogImageId
	 */
	@GET
	@Path("/download/catalogImage")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("catalogImageId") Long catalogImageId, @Context HttpServletRequest req) {
		// パーツデザイン時にハイパーリンクパーツで添付したファイル
		// プロセスIDに依存しない
		MwmCatalogImage entity = catalogService.getCatalogImage(catalogImageId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	@Path("/insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0011Response insert(Ct0011Request req) {
		return service.insert(req);
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0011Response update(Ct0011Request req) {
		return service.update(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ct0011Response delete(Ct0011Request req) {
		return service.delete(req);
	}

}
