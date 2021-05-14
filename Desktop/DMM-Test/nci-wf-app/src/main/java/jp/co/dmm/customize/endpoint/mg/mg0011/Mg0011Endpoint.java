package jp.co.dmm.customize.endpoint.mg.mg0011;

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

import jp.co.dmm.customize.jpa.entity.mw.ItmImgMst;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * 品目マスタ設定画面Endpoint
 */
@Path("/mg0011")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0011Endpoint extends BaseEndpoint<Mg0011GetRequest> {

	@Inject private Mg0011Service service;

	/**
	 * 品目マスタ設定画面初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0011GetResponse init(Mg0011GetRequest req) {
		return service.init(req);
	}

	/**
	 * 品目マスタ更新処理
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0011UpdateResponse update(Mg0011UpdateRequest req) {
		return service.update(req);
	}

	/**
	 * 品目マスタ登録処理
	 */
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0011UpdateResponse insert(Mg0011UpdateRequest req) {
		return service.insert(req);
	}

	/**
	 * 品目チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/insertCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertCheck(Mg0011UpdateRequest req) {
		return service.insertCheck(req);
	}

	/**
	 * 品目チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/updateCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCheck(Mg0011UpdateRequest req) {
		return service.updateCheck(req);
	}

	/**
	 * ファイルアップロード
	 * @param bodyParts
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0011UploadResponse upload(@FormDataParam("file") List<FormDataBodyPart> bodyParts) {
		return service.upload(bodyParts);
	}

	/**
	 * 画像
	 * @param catalogImageId
	 */
	@GET
	@Path("/download/itemImage")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("itmImgId") Long itmImgId, @Context HttpServletRequest req) {
		// パーツデザイン時にハイパーリンクパーツで添付したファイル
		// プロセスIDに依存しない
		ItmImgMst entity = service.getItemImage(itmImgId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return DownloadUtils.download(entity.getFileNm(), entity.getFileData());
	}

}
