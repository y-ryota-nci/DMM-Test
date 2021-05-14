package jp.co.nci.iwf.endpoint.cm.cm0170;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.BbsAttachFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 要説明(掲示板)ブロック用、投稿内容入力画面Endpoint
 */
@Endpoint
@Path("/cm0170")
@RequiredLogin
@WriteAccessLog
public class Cm0170Endpoint extends BaseEndpoint<Cm0170Request> {
	@Inject private Cm0170Service service;

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
	public Cm0170Response init(Cm0170Request req) {
		return service.init(req);
	}

	/**
	 * 添付ファイルアップロード処理
	 * @param bodyParts
	 * @return
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public List<BbsAttachFileWfInfo> upload(@FormDataParam("file") List<FormDataBodyPart> bodyParts) {
		return service.upload(bodyParts);
	}


	/**
	 * 添付ファイルのファイルダウンロード
	 * @param bbsAttachFileWfId ワークフローパーツ添付ファイルID
	 */
	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("bbsAttachFileWfId") Long bbsAttachFileWfId) {
		MwtBbsAttachFileWf entity = service.download(bbsAttachFileWfId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileName(), entity.getFileData());
	}

}
