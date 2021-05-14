package jp.co.nci.iwf.endpoint.rm.rm0510;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.Dt;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleMenu;

/**
 * 表示条件設定サービス
 */
@BizLogic
public class Rm0510Service extends MmBaseService<WfmMenuRole> {
	// TODO：多言語
//	@Inject
//	private MultilingalService multi;

	@Inject
	Rm0510Repository repository;
	@Inject
	private NumberingService numbering;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Rm0510Response init(Rm0510Request req) {
		final Rm0510Response res = this.createResponse(Rm0510Response.class, req);

		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.menuRoleCode)) {
			throw new BadRequestException("メニューロールコードが未指定です");
		}

		SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setMenuRoleCode(req.menuRoleCode);
		inParam.setMenuRoleType(MenuRoleType.CORPORATION);
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(inParam);
		final List<WfmMenuRole> list = outParam.getMenuRoles();
		if (CommonUtil.isEmpty(list)) {
			throw new BadRequestException("存在しないメニューロールです");
		}
		res.menuRole = list.get(0);

		// メニュー取得
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.accessibleMenus = repository.search(req.corporationCode, req.menuRoleCode, MenuRoleType.CORPORATION, loginInfo.getLocaleCode());
		res.success = true;

		return res;
	}

	/**
	 * 登録処理
	 * @param req
	 * @return
	 */
	@Transactional
	public Rm0510Response save(Rm0510SaveRequest req) {
		final Rm0510Response res = createResponse(Rm0510Response.class, req);

		repository.delete(req.corporationCode, req.menuRoleCode);
		for (MwmMenuEx menu : req.accessibleMenuList) {
			if (Boolean.parseBoolean(menu.getExist())) {
				MwmAccessibleMenu accessible = new MwmAccessibleMenu();
				accessible.setAccessibleMenuId(numbering.newPK(MwmAccessibleMenu.class));
				accessible.setCorporationCode(req.corporationCode);
				accessible.setMenuId(menu.getMenuId());
				accessible.setMenuRoleCode(req.menuRoleCode);
				accessible.setValidStartDate(Dt.MIN);
				accessible.setValidEndDate(Dt.MAX);
				accessible.setDeleteFlag(DeleteFlag.OFF);
				repository.insert(accessible);
			}
		}

		// メニュー取得
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.accessibleMenus = repository.search(req.corporationCode, req.menuRoleCode, MenuRoleType.CORPORATION, loginInfo.getLocaleCode());

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.accessibleMenu));
		res.success = true;
		return res;
	}
}
