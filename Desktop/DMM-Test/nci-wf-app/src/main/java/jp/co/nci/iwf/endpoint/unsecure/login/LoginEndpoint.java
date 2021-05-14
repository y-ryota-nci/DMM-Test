package jp.co.nci.iwf.endpoint.unsecure.login;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ログイン認証画面Endpoint
 */
@Path("/login")
@Endpoint
@WriteAccessLog
public class LoginEndpoint extends BaseEndpoint<BaseRequest>{
	@Inject
	private LoginService service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	@Path("/init")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginInitResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * ログイン認証
	 * @param req
	 * @return
	 */
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(LoginRequest req) {
		return service.login(req);
	}

	/**
	 * ログアウト処理
	 * @return
	 */
	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse logout() {
		return service.logout();
	}

	/**
	 * 言語の変更
	 * @param newLocaleCode
	 * @return
	 */
	@Path("/changeLocaleCode")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse changeLocaleCode(@QueryParam("newLocaleCode") String newLocaleCode) {
		return service.changeLocaleCode(newLocaleCode);
	}

	/**
	 * （DMM専用）企業の切り替え
	 * @param newCorporationCode
	 * @return
	 */
	@Path("/changeCorporation")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse changeCorporation(LoginChangeCorporationRequest req) {
		return service.changeCorporation(req);
	}
}
