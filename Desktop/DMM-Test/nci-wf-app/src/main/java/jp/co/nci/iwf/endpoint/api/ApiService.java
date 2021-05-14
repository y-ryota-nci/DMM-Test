package jp.co.nci.iwf.endpoint.api;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.api.param.input.CreateProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetActionHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetAvailableActionListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetPullbackActivityDefListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetRouteDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetRouteInParam;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.PullBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SendBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetAdditionalInfoInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetVariableValueInParam;
import jp.co.nci.integrated_workflow.api.param.input.StopProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActionHistoryListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetAvailableActionListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetPullbackActivityDefListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetRouteDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetRouteOutParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.PullBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SendBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SetAdditionalInfoOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SetVariableValueOutParam;
import jp.co.nci.integrated_workflow.api.param.output.StopProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
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
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * WF API実行サービス
 */
@BizLogic
public class ApiService extends BaseService {

	@Inject
	private WfInstanceWrapper wf;

	/**
	 * 起票
	 * @param req
	 * @return
	 */
	@Transactional
	public CreateProcessInstanceResponse createProcessInstance(CreateProcessInstanceRequest req) {
		CreateProcessInstanceInParam inParam = new CreateProcessInstanceInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		CreateProcessInstanceOutParam outParam = wf.createProcessInstance(inParam);
		CreateProcessInstanceResponse res = new CreateProcessInstanceResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 状態遷移
	 * @param req
	 * @return
	 */
	@Transactional
	public MoveActivityInstanceResponse moveActivityInstance(MoveActivityInstanceRequest req) {
		MoveActivityInstanceInParam inParam = new MoveActivityInstanceInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		MoveActivityInstanceOutParam outParam = wf.moveActivityInstance(inParam);
		MoveActivityInstanceResponse res = new MoveActivityInstanceResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 差戻し
	 * @param req
	 * @return
	 */
	@Transactional
	public SendbackActivityInstanceResponse sendbackActivityInstance(SendbackActivityInstanceRequest req) {
		SendBackActivityInstanceInParam inParam = new SendBackActivityInstanceInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		SendBackActivityInstanceOutParam outParam = wf.sendBackActivityInstance(inParam);
		SendbackActivityInstanceResponse res = new SendbackActivityInstanceResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 引戻し
	 * @param req
	 * @return
	 */
	@Transactional
	public PullbackActivityInstanceResponse pullbackActivityInstance(PullbackActivityInstanceRequest req) {
		PullBackActivityInstanceInParam inParam = new PullBackActivityInstanceInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		PullBackActivityInstanceOutParam outParam = wf.pullBackActivityInstance(inParam);
		PullbackActivityInstanceResponse res = new PullbackActivityInstanceResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 却下
	 * @param req
	 * @return
	 */
	@Transactional
	public StopProcessInstanceResponse stopProcessInstance(StopProcessInstanceRequest req) {
		StopProcessInstanceInParam inParam = new StopProcessInstanceInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		StopProcessInstanceOutParam outParam = wf.stopProcessInstance(inParam);
		StopProcessInstanceResponse res = new StopProcessInstanceResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * トレイ照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetActivityListResponse getActivityList(GetActivityListRequest req) {
		GetActivityListInParam inParam = new GetActivityListInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetActivityListOutParam outParam = wf.getActivityList(inParam);
		GetActivityListResponse res = new GetActivityListResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 承認履歴照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetActionHistoryListResponse getActionHistoryList(GetActionHistoryListRequest req) {
		GetActionHistoryListInParam inParam = new GetActionHistoryListInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetActionHistoryListOutParam outParam = wf.getActionHistoryList(inParam);
		GetActionHistoryListResponse res = new GetActionHistoryListResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * プロセス履歴照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetProcessHistoryListResponse getProcessHistoryList(GetProcessHistoryListRequest req) {
		GetProcessHistoryListInParam inParam = new GetProcessHistoryListInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetProcessHistoryListOutParam outParam = wf.getProcessHistoryList(inParam);
		GetProcessHistoryListResponse res = new GetProcessHistoryListResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 承認ルート定義照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetRouteDefResponse getRouteDef(GetRouteDefRequest req) {
		GetRouteDefInParam inParam = new GetRouteDefInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetRouteDefOutParam outParam = wf.getRouteDef(inParam);
		GetRouteDefResponse res = new GetRouteDefResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * 承認ルート照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetRouteResponse getRoute(GetRouteRequest req) {
		GetRouteInParam inParam = new GetRouteInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetRouteOutParam outParam = wf.getRoute(inParam);
		GetRouteResponse res = new GetRouteResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 * アクション一覧照会
	 * @param req
	 * @return
	 */
	@Transactional
	public GetAvailableActionListResponse getAvailableActionList(GetAvailableActionListRequest req) {
		GetAvailableActionListInParam inParam = new GetAvailableActionListInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetAvailableActionListOutParam outParam = wf.getAvailableActionList(inParam);
		GetAvailableActionListResponse res = new GetAvailableActionListResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 *
	 * @param req
	 * @return
	 */
	@Transactional
	public GetPullbackActivityDefListResponse getPullbackActivityDefList(GetPullbackActivityDefListRequest req) {
		GetPullbackActivityDefListInParam inParam = new GetPullbackActivityDefListInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		GetPullbackActivityDefListOutParam outParam = wf.getPullbackActivityDefList(inParam);
		GetPullbackActivityDefListResponse res = new GetPullbackActivityDefListResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 *
	 * @param req
	 * @return
	 */
	@Transactional
	public SetVariableValueResponse setVariableValue(SetVariableValueRequest req) {
		SetVariableValueInParam inParam = new SetVariableValueInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		SetVariableValueOutParam outParam = wf.setVariableValue(inParam);
		SetVariableValueResponse res = new SetVariableValueResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

	/**
	 *
	 * @param req
	 * @return
	 */
	@Transactional
	public SetAdditionalInfoResponse setAdditionalInfo(SetAdditionalInfoRequest req) {
		SetAdditionalInfoInParam inParam = new SetAdditionalInfoInParam();

		try {
			BeanUtils.copyProperties(inParam, req);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		inParam.setWfUserRole(req.getWfUserRole());
		SetAdditionalInfoOutParam outParam = wf.setAdditionalInfo(inParam);
		SetAdditionalInfoResponse res = new SetAdditionalInfoResponse();

		try {
			BeanUtils.copyProperties(res, outParam);
		} catch (InvocationTargetException ite) {
			throw new InternalServerErrorException(ite);
		} catch (IllegalAccessException iae) {
			throw new InternalServerErrorException(iae);
		}

		return res;
	}

}
