package jp.co.dmm.customize.endpoint.sp.sp0010;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;

/**
 * 取引先一覧Endpoint
 */
@Path("/sp0010")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Sp0010Endpoint extends BaseEndpoint<Sp0010SearchRequest> {

	@Inject private Sp0010Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Sp0010SearchResponse init(Sp0010SearchRequest req) {
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
	public Sp0010SearchResponse search(Sp0010SearchRequest req) {
		return service.search(req);
	}

	/**
	 * 会社情報取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0010GetcompanyResponse getCompany(Sp0010GetcompanyRequest req) {
		return service.getCompany(req);
	}

	/**
	 * 取引先口座情報チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/accountCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0010AccountCheckResponse accountCheck(Sp0010AccountCheckRequest req) {
		return service.accountCheck(req);
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0010GetScreenProcessIdResponse validate(Sp0010GetScreenProcessIdRequest req) {
		return service.validate(req);
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getScreenProcessId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0010GetScreenProcessIdResponse getScreenProcessId(Sp0010GetScreenProcessIdRequest req) {
		return service.getScreenProcessId(req);
	}


	/**
	 * 取引先チェック
	 * @param req
	 * @return
	 */
	@POST
	@Path("/researchCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Sp0010ResearchCheckResponse researchCheck(Sp0010ResearchCheckRequest req) {
		return service.researchCheck(req);
	}

	/**
	 * 住所取得
	 */
	@POST
	@Path("/getAddressInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Sp0010GetAddressResponse getAddressInfo(Sp0010GetAddressRequest req) {
		return service.getAddressInfo(req);
	}

	/**
	 *
	 */
	@POST
	@Path("/getDefaultBnkacc")
	@Consumes(MediaType.APPLICATION_JSON)
	public Sp0010GetDefaultBnkaccResponse getDefaultBnkacc(Sp0010GetDefaultBnkaccRequest req) {
		return service.getDefaultBnkacc(req);
	}

}
