package jp.co.dmm.customize.endpoint.mg.mg0200;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 銀行カレンダマスタ登録Endpoint
 */
@Path("/mg0200")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0200Endpoint extends BaseEndpoint<Mg0200Request> {

	@Inject private Mg0200Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(Mg0200Request req) {
		return service.init(req);
	}

	/**
	 * 会計カレンダマスタ検索
	 */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0200Response search(Mg0200Request req) {
		return service.search(req);
	}

	/**
	 * 会計カレンダマスタ生成
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0200Response create(Mg0200Request req) {
		return service.create(req);
	}

	/**
	 * 会計カレンダマスタ更新
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mg0200Response update(Mg0200Request req) {
		return service.update(req);
	}

	/**
	 * テンプレートのダウンロード
	 */
	@POST
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response download(Mg0200Request req) {
		return service.download(req);
	}

	/**
	 * クレカ口座情報アップロード
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
	 * クレカ口座情報の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(MgUploadSaveRequest req) {
		return service.register(req);
	}
}
