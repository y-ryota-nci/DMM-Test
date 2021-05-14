package jp.co.nci.iwf.endpoint.dc.dc0101;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書ファイル更新Endpoint.
 */
@Path("/dc0101")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0101Endpoint extends BaseEndpoint<Dc0101InitRequest> {


	@Inject private Dc0101Service service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0101InitResponse init(Dc0101InitRequest req) {
		return service.init(req);
	}

	/**
	 * 文書ファイルロック処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/lock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lock(DocFileOperationRequest req) {
		return service.lock(req);
	}

	/**
	 * 文書ファイルロック解除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/unlock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlock(DocFileOperationRequest req) {
		return service.unlock(req);
	}

	/**
	 * 文書ファイルデータアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * 文書ファイルデータダウンロード
	 * @param attachFileWfId
	 */
	@POST
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("docFileDataId") Long docFileDataId) {
		return service.download(docFileDataId);
	}

	/**
	 * 文書ファイル更新
	 * @param req
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Dc0101ExecuteRequest req) {
		return service.update(req);
	}

	/**
	 * 文書ファイル削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(DocFileOperationRequest req) {
		return service.delete(req);
	}

}
