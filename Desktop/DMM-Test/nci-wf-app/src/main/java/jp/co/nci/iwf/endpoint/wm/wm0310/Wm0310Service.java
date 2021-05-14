package jp.co.nci.iwf.endpoint.wm.wm0310;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmChangeRoleOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.base.impl.WfmChangeRoleImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 参加者変更ロール登録サービス
 */
@BizLogic
public class Wm0310Service extends BaseService {
	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wm0310Response init(Wm0310Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Wm0310Response res = createResponse(Wm0310Response.class, req);
		res.changeRole = new WfmChangeRoleImpl();
		res.changeRole.setCorporationCode(req.corporationCode);
		res.changeRole.setValidStartDate(today());
		res.changeRole.setValidEndDate(ENDDATE);
		res.changeRole.setDeleteFlag(DeleteFlag.OFF);
		res.success = (res.changeRole != null);
		return res;
	}

	/**
	 * 参加者変更ロールの登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Wm0310Response insert(Wm0310InsertRequest req) {
		final Wm0310Response res = createResponse(Wm0310Response.class, req);

		SearchWfmChangeRoleInParam inParam = new SearchWfmChangeRoleInParam();
		inParam.setCorporationCode(req.changeRole.getCorporationCode());
		inParam.setChangeRoleCode(req.changeRole.getChangeRoleCode());
		inParam.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmChangeRoleOutParam outParam = wf.searchWfmChangeRole(inParam);

		if (!CommonUtil.isEmpty(outParam.getChangeRoles())) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.changeRoleCode, req.changeRole.getChangeRoleCode()));
			res.success = false;
			return res;

		}

		InsertWfmChangeRoleInParam insertIn = new InsertWfmChangeRoleInParam();
		insertIn.setWfmChangeRole(req.changeRole);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmChangeRoleOutParam insertOut = wf.insertWfmChangeRole(insertIn);

		res.changeRole = insertOut.getWfmChangeRole();
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.changeRole));
		res.success = true;
		return res;
	}
}
