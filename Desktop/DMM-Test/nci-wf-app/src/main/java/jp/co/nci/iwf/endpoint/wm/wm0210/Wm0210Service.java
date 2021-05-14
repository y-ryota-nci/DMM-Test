package jp.co.nci.iwf.endpoint.wm.wm0210;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmAssignRoleOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.base.impl.WfmAssignRoleImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 参加者ロール登録サービス
 */
@BizLogic
public class Wm0210Service extends BaseService {
	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0210Response init(Wm0210Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Wm0210Response res = createResponse(Wm0210Response.class, req);
		res.assignRole = new WfmAssignRoleImpl();
		res.assignRole.setCorporationCode(req.corporationCode);
		res.assignRole.setValidStartDate(today());
		res.assignRole.setValidEndDate(ENDDATE);
		res.assignRole.setDeleteFlag(DeleteFlag.OFF);
		res.success = (res.assignRole != null);
		return res;
	}

	/**
	 * 参加者ロールの登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0210Response insert(Wm0210InsertRequest req) {
		final Wm0210Response res = createResponse(Wm0210Response.class, req);

		SearchWfmAssignRoleInParam inParam = new SearchWfmAssignRoleInParam();
		inParam.setCorporationCode(req.assignRole.getCorporationCode());
		inParam.setAssignRoleCode(req.assignRole.getAssignRoleCode());
		inParam.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmAssignRoleOutParam outParam = wf.searchWfmAssignRole(inParam);

		if (!CommonUtil.isEmpty(outParam.getAssignRoles())) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.assignRoleCode, req.assignRole.getAssignRoleCode()));
			res.success = false;
			return res;

		}

		InsertWfmAssignRoleInParam insertIn = new InsertWfmAssignRoleInParam();
		insertIn.setWfmAssignRole(req.assignRole);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmAssignRoleOutParam insertOut = wf.insertWfmAssignRole(insertIn);

		res.assignRole = insertOut.getWfmAssignRole();
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.assignRole));
		res.success = true;
		return res;
	}
}
