package jp.co.nci.iwf.endpoint.au.au0012;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.endpoint.au.au0011.Au0011Request;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パスワード変更画面(管理者)Endpoint
 */
@Path("/au0012")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Au0012Endpoint extends BaseEndpoint<Au0011Request> {
	@Inject
	private Au0012Service service;

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
	public BaseResponse init(Au0011Request req) {
		return service.init(req);
	}

	/**
	 * パスワードの保存
	 * @param req
	 * @return
	 */
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse save(Au0011Request req) {
		return service.save(req);
	}

	/**
	 * 次回ログイン時に変更を強制
	 * @param req
	 * @return
	 */
	@Path("/changePasswordRequest")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse changePasswordRequest(Au0011Request req) {
		return service.changePasswordRequest(req);
	}

	/**
	 * リセット＆新パスワードをメール送信
	 * @param req
	 * @return
	 */
	@Path("/resetPassword")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse resetPassword(Au0011Request req) {
		return service.resetPassword(req);
	}

	/**
	 * アカウントロック
	 * @param req
	 * @return
	 */
	@Path("/lockAccount")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse lockAccount(Au0011Request req) {
		return service.lockAccount(req);
	}

	/**
	 * アカウントロック解除
	 * @param req
	 * @return
	 */
	@Path("/unlockAccount")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse unlockAccount(Au0011Request req) {
		return service.unlockAccount(req);
	}
}
