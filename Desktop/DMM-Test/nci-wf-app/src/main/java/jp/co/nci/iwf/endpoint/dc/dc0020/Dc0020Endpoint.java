package jp.co.nci.iwf.endpoint.dc.dc0020;

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

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.component.document.DocFileOperationRequest;
import jp.co.nci.iwf.component.document.DocInfoOperationRequest;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 書類一覧設定Endpoint
 */
@Path("/dc0020")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Dc0020Endpoint extends BaseEndpoint<Dc0020InitRequest> {

	@Inject
	private Dc0020Service service;

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Dc0020InitResponse init(Dc0020InitRequest req) {
		return service.init(req);
	}

	@GET
	@Path("/getTreeItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dc0020TreeItem> getTreeItems(@QueryParam("nodeId") String nodeId) {
		return service.getTreeItems(nodeId);
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0020SearchResponse search(Dc0020SearchRequest req) {
		return service.search(req);
	}

	@POST
	@Path("/simpleSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Dc0020SearchResponse simpleSearch(Dc0020SearchRequest req) {
		return service.search(req);
	}

	/**
	 * ロック処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/lock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lock(DocInfoOperationRequest req) {
		return service.lock(req);
	}

	/**
	 * ロック解除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/unlock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlock(DocInfoOperationRequest req) {
		return service.unlock(req);
	}

	/**
	 * コピー
	 * @param req
	 * @return
	 */
	@POST
	@Path("/copy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse copy(Dc0020OperationRequest req) {
		return service.copy(req);
	}

	/**
	 * 移動
	 * @param req
	 * @return
	 */
	@POST
	@Path("/move")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse move(Dc0020OperationRequest req) {
		return service.move(req);
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse delete(Dc0020OperationRequest req) {
		return service.delete(req);
	}

	/**
	 * 文書ファイルロック処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/lockFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lockFile(DocFileOperationRequest req) {
		return service.lock(req);
	}

	/**
	 * 文書ファイルロック解除
	 * @param req
	 * @return
	 */
	@POST
	@Path("/unlockFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlockFile(DocFileOperationRequest req) {
		return service.unlock(req);
	}

	/**
	 * 文書ファイルダウンロード
	 * @param docFileDataId
	 */
	@POST
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@QueryParam("docFileDataId") Long docFileDataId) {
		return service.download(docFileDataId);
	}
}
