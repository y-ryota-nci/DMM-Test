package jp.co.dmm.customize.endpoint.mg.mg0210;

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
 * 源泉税区分マスタ一覧Endpoint
 */
@Path("/mg0210")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mg0210Endpoint extends BaseEndpoint<Mg0210Request> {

	@Inject private Mg0210Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Mg0210Response init(Mg0210Request req) {
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
	public Mg0210Response search(Mg0210Request req) {
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
	public Mg0210Response delete(Mg0210Request req) {
		return service.delete(req);
	}

	/**
	 * テンプレートのダウンロード
	 */
	@POST
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response download(Mg0210Request req) {
		return service.download(req);
	}

	/**
	 * 源泉税情報アップロード
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
	 * 源泉税情報の登録
	 */
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(MgUploadSaveRequest req) {
		return service.register(req);
	}

}
