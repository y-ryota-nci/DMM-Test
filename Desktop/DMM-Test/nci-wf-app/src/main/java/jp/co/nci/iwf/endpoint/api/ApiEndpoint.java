package jp.co.nci.iwf.endpoint.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.endpoint.api.request.CreateProcessInstanceRequest;
import jp.co.nci.iwf.endpoint.api.request.GetActionHistoryListRequest;
import jp.co.nci.iwf.endpoint.api.request.GetActivityListRequest;
import jp.co.nci.iwf.endpoint.api.request.GetAvailableActionListRequest;
import jp.co.nci.iwf.endpoint.api.request.GetProcessHistoryListRequest;
import jp.co.nci.iwf.endpoint.api.request.GetPullbackActivityDefListRequest;
import jp.co.nci.iwf.endpoint.api.request.GetRouteDefRequest;
import jp.co.nci.iwf.endpoint.api.request.GetRouteRequest;
import jp.co.nci.iwf.endpoint.api.request.MoveActivityInstanceRequest;
import jp.co.nci.iwf.endpoint.api.request.PullbackActivityInstanceRequest;
import jp.co.nci.iwf.endpoint.api.request.SendbackActivityInstanceRequest;
import jp.co.nci.iwf.endpoint.api.request.SetAdditionalInfoRequest;
import jp.co.nci.iwf.endpoint.api.request.SetVariableValueRequest;
import jp.co.nci.iwf.endpoint.api.request.StopProcessInstanceRequest;
import jp.co.nci.iwf.endpoint.api.response.CreateProcessInstanceResponse;
import jp.co.nci.iwf.endpoint.api.response.GetActionHistoryListResponse;
import jp.co.nci.iwf.endpoint.api.response.GetActivityListResponse;
import jp.co.nci.iwf.endpoint.api.response.GetAvailableActionListResponse;
import jp.co.nci.iwf.endpoint.api.response.GetProcessHistoryListResponse;
import jp.co.nci.iwf.endpoint.api.response.GetPullbackActivityDefListResponse;
import jp.co.nci.iwf.endpoint.api.response.GetRouteDefResponse;
import jp.co.nci.iwf.endpoint.api.response.GetRouteResponse;
import jp.co.nci.iwf.endpoint.api.response.MoveActivityInstanceResponse;
import jp.co.nci.iwf.endpoint.api.response.PullbackActivityInstanceResponse;
import jp.co.nci.iwf.endpoint.api.response.SendbackActivityInstanceResponse;
import jp.co.nci.iwf.endpoint.api.response.SetAdditionalInfoResponse;
import jp.co.nci.iwf.endpoint.api.response.SetVariableValueResponse;
import jp.co.nci.iwf.endpoint.api.response.StopProcessInstanceResponse;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * WF API実行Endpoint.
 */
@Endpoint
@Path("/api")
public class ApiEndpoint extends BaseEndpoint<BaseRequest> {
	@Inject
	private ApiService service;

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
	public BaseResponse init(BaseRequest req) {
		return null;
	}

	/**
	 * 起票
	 * @param req
	 * @return
	 */
	@Path("/createProcessInstance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateProcessInstanceResponse createProcessInstance(CreateProcessInstanceRequest req) {
		return service.createProcessInstance(req);
	}

	/**
	 * 状態遷移
	 * @param req
	 * @return
	 */
	@Path("/moveActivityInstance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MoveActivityInstanceResponse moveActivityInstance(MoveActivityInstanceRequest req) {
		return service.moveActivityInstance(req);
	}

	/**
	 * 差戻
	 * @param req
	 * @return
	 */
	@Path("/sendbackActivityInstance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SendbackActivityInstanceResponse sendbackActivityInstance(SendbackActivityInstanceRequest req) {
		return service.sendbackActivityInstance(req);
	}

	/**
	 * 引戻
	 * @param req
	 * @return
	 */
	@Path("/pullbackActivityInstance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PullbackActivityInstanceResponse pullbackActivityInstance(PullbackActivityInstanceRequest req) {
		return service.pullbackActivityInstance(req);
	}

	/**
	 * 却下
	 * @param req
	 * @return
	 */
	@Path("/stopProcessInstance")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public StopProcessInstanceResponse stopProcessInstance(StopProcessInstanceRequest req) {
		return service.stopProcessInstance(req);
	}

	/**
	 * トレイ照会
	 * @param req
	 * @return
	 */
	@Path("/getActivityList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetActivityListResponse getActivityList(GetActivityListRequest req) {
		return service.getActivityList(req);
	}

	/**
	 * 承認履歴照会
	 * @param req
	 * @return
	 */
	@Path("/getActionHistoryList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetActionHistoryListResponse getActionHistoryList(GetActionHistoryListRequest req) {
		return service.getActionHistoryList(req);
	}

	/**
	 * プロセス履歴照会
	 * @param req
	 * @return
	 */
	@Path("/getProcessHistoryList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetProcessHistoryListResponse getProcessHistoryList(GetProcessHistoryListRequest req) {
		return service.getProcessHistoryList(req);
	}

	/**
	 * 承認ルート定義照会
	 * @param req
	 * @return
	 */
	@Path("/getRouteDef")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetRouteDefResponse getRouteDef(GetRouteDefRequest req) {
		return service.getRouteDef(req);
	}

	/**
	 * 承認ルート照会
	 * @param req
	 * @return
	 */
	@Path("/getRoute")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetRouteResponse getRoute(GetRouteRequest req) {
		return service.getRoute(req);
	}

	/**
	 * アクション一覧照会
	 * @param req
	 * @return
	 */
	@Path("/getAvailableActionList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetAvailableActionListResponse getAvailableActionList(GetAvailableActionListRequest req) {
		return service.getAvailableActionList(req);
	}

	/**
	 * 引戻先アクティビティ定義照会.
	 * @param req
	 * @return
	 */
	@Path("/getPullbackActivityDefList")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetPullbackActivityDefListResponse getPullbackActivityDefList(GetPullbackActivityDefListRequest req) {
		return service.getPullbackActivityDefList(req);
	}

	/**
	 * 比較条件式変数設定.
	 * @param req
	 * @return
	 */
	@Path("/setVariableValue")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SetVariableValueResponse setVariableValue(SetVariableValueRequest req) {
		return service.setVariableValue(req);
	}

	/**
	 * 付加情報設定.
	 * @param req
	 * @return
	 */
	@Path("/setAdditionalInfo")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SetAdditionalInfoResponse setAdditionalInfo(SetAdditionalInfoRequest req) {
		return service.setAdditionalInfo(req);
	}

}
