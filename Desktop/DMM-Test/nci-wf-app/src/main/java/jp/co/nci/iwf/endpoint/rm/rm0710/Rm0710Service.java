package jp.co.nci.iwf.endpoint.rm.rm0710;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.base.impl.WfmMenuRoleImpl;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 利用者ロール登録サービス
 */
@BizLogic
public class Rm0710Service extends MmBaseService<WfmMenuRole> {
	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0710Response init(Rm0710Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Rm0710Response res = createResponse(Rm0710Response.class, req);

		res.menuRole = new WfmMenuRoleImpl();
		res.menuRole.setMenuRoleType(MenuRoleType.CORPORATION);
		res.menuRole.setCorporationCode(req.corporationCode);
		res.menuRole.setValidStartDate(today());
		res.menuRole.setValidEndDate(ENDDATE);
		res.menuRole.setDeleteFlag(DeleteFlag.OFF);

		res.corporations = getAccessibleCorporations(false);

		res.success = (res.menuRole != null);
		return res;
	}

	/**
	 * 参加者ロールの登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0710Response insert(Rm0710InsertRequest req) {
		final Rm0710Response res = createResponse(Rm0710Response.class, req);

		SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(req.menuRole.getCorporationCode());
		inParam.setMenuRoleCode(req.menuRole.getMenuRoleCode());
		inParam.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(inParam);

		if (!CommonUtil.isEmpty(outParam.getMenuRoles())) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.menuRoleCode, req.menuRole.getMenuRoleCode()));
			res.success = false;
			return res;

		}

		InsertWfmMenuRoleInParam insertIn = new InsertWfmMenuRoleInParam();
		insertIn.setWfmMenuRole(req.menuRole);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmMenuRoleOutParam insertOut = wf.insertWfmMenuRole(insertIn);

		res.menuRole = insertOut.getWfmMenuRole();
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.menuRole));
		res.success = true;
		return res;
	}
}
