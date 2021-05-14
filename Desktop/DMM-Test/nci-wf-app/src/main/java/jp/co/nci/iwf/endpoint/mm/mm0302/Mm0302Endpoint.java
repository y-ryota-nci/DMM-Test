package jp.co.nci.iwf.endpoint.mm.mm0302;

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
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ルート編集Endpoint
 */
@Path("/mm0302")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Mm0302Endpoint extends BaseEndpoint<Mm0302InitRequest>{

	@Inject
	private Mm0302Service service;

	/**
	 * 初期化処理
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse init(Mm0302InitRequest req) {
		return service.init(req);
	}

	/**
	 * プロセス定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ProcessDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ProcessDefRequest req) {
		return service.update(req);
	}

	/**
	 * プロセス定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ProcessDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ProcessDefRequest req) {
		return service.remove(req);
	}

	/**
	 * 比較条件式定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ExpressionDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ExpressionDefRequest req) {
		return service.update(req);
	}

	/**
	 * 比較条件式定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ExpressionDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ExpressionDefRequest req) {
		return service.remove(req);
	}

	/**
	 * アクティビティ定義作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ActivityDef/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse create(Mm0302ActivityDefRequest req) {
		return service.create(req);
	}

	/**
	 * アクティビティ定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ActivityDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ActivityDefRequest req) {
		return service.update(req);
	}

	/**
	 * アクティビティ定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ActivityDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ActivityDefRequest req) {
		return service.remove(req);
	}

	/**
	 * 参加者定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/AssignedDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302AssignedDefRequest req) {
		return service.update(req);
	}

	/**
	 * 参加者定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/AssignedDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302AssignedDefRequest req) {
		return service.remove(req);
	}

	/**
	 * 参加者変更定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ChangeDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ChangeDefRequest req) {
		return service.update(req);
	}

	/**
	 * 参加者変更定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ChangeDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ChangeDefRequest req) {
		return service.remove(req);
	}

	/**
	 * アクション定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ActionDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ActionDefRequest req) {
		return service.update(req);
	}

	/**
	 * アクション定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ActionDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ActionDefRequest req) {
		return service.remove(req);
	}

	/**
	 * アクション遷移先定義作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ConditionDef/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse create(Mm0302ConditionDefRequest req) {
		return service.create(req);
	}

	/**
	 * アクション遷移先定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ConditionDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302ConditionDefRequest req) {
		return service.update(req);
	}

	/**
	 * アクション遷移先定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/ConditionDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302ConditionDefRequest req) {
		return service.remove(req);
	}

	/**
	 * アクション機能定義更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/FunctionDef/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse update(Mm0302FunctionDefRequest req) {
		return service.update(req);
	}

	/**
	 * アクション機能定義削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@POST
	@Path("/FunctionDef/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BaseResponse remove(Mm0302FunctionDefRequest req) {
		return service.remove(req);
	}

}
