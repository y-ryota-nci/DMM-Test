package jp.co.dmm.customize.endpoint.mg.mg0270;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgUploadSaveRequest;
import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 予算科目マスタ一覧Endpoint
 */
@Path("/mg0270")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0270Endpoint extends BaseEndpoint<Mg0270SearchRequest> {

	@Inject private Mg0270Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0270SearchResponse init(Mg0270SearchRequest req) {
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
	public Mg0270SearchResponse search(Mg0270SearchRequest req) {
		return service.search(req);
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
	public Mg0270RemoveResponse delete(Mg0270RemoveRequest req) {
		return service.delete(req);
	}

	/**
	 * テンプレートのダウンロード
	 */
	@POST
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response download(Mg0270SearchRequest req) {
		return service.download(req);
	}

	/**
	 * 予算科目情報アップロード
	 * @param multiPart
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(FormDataMultiPart multiPart) {
		return service.upload(multiPart);
	}

	/**
	 * 予算科目情報の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(MgUploadSaveRequest req) {
		return service.register(req);
	}
}
