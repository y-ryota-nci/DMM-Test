package jp.co.nci.iwf.endpoint.ti.ti0021;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableAuthority;

/**
 * マスタ権限設定Service
 */
@BizLogic
public class Ti0021Service extends BaseService {
	@Inject private Ti0021Repository repository;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ti0021Response init(Ti0021InitRequest req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.menuRoleCode))
			throw new BadRequestException("メニューロールCDが未指定です");

		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Ti0021Response res = createResponse(Ti0021Response.class, req);

		// メニューロール
		res.menuRole = getWfmMenuRole(req.corporationCode, req.menuRoleCode);
		// カテゴリ権限
		res.categories = repository.getCategories(req.corporationCode, req.menuRoleCode, localeCode);
		// テーブル権限
		res.tables = repository.getTables(req.corporationCode, req.menuRoleCode, localeCode);
		res.success = true;

		return res;
	}

	/** メニューロールを抽出 */
	private WfmMenuRole getWfmMenuRole(String corporationCode, String menuRoleCode) {
		final SearchWfmMenuRoleInParam in = new SearchWfmMenuRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setMenuRoleCode(menuRoleCode);
		SearchWfmMenuRoleOutParam out = wf.searchWfmMenuRole(in);
		final List<WfmMenuRole> list = out.getMenuRoles();
		if (CommonUtil.isEmpty(list)) {
			throw new BadRequestException("存在しないメニューロールです");
		}
		WfmMenuRole mr = list.get(0);
		return mr;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public Ti0021Response save(Ti0021Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String localeCode = login.getLocaleCode();
		final String corporationCode = req.corporationCode;
		final String menuRoleCode = req.menuRoleCode;

		// カテゴリ権限
		{
			final Map<Long, MwmCategoryAuthority> currents =
					repository.getMwmCategoryAuthority(corporationCode, menuRoleCode);
			for (Ti0021Category input : req.categories) {
				MwmCategoryAuthority current = currents.remove(input.categoryAuthorityId);
				if (current == null)
					repository.insert(corporationCode, menuRoleCode, input);
				else
					repository.update(current, input);
			}
			for (MwmCategoryAuthority c : currents.values()) {
				repository.delete(c);
			}
		}
		// テーブル権限
		{
			final Map<Long, MwmTableAuthority> currents =
					repository.getMwmTableAuthority(corporationCode, menuRoleCode);
			for (Ti0021Table input : req.tables) {
				MwmTableAuthority current = currents.remove(input.tableAuthorityId);
				if (current == null)
					repository.insert(corporationCode, menuRoleCode, input);
				else
					repository.update(current, input);
			}
			for (MwmTableAuthority ta : currents.values()) {
				repository.delete(ta);
			}
		}

		// レスポンス
		final Ti0021Response res = createResponse(Ti0021Response.class, req);
		// メニューロール
		res.menuRole = getWfmMenuRole(corporationCode, menuRoleCode);
		// カテゴリ権限
		res.categories = repository.getCategories(corporationCode, menuRoleCode, localeCode);
		// テーブル権限
		res.tables = repository.getTables(corporationCode, menuRoleCode, localeCode);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.masterAuthority));

		return res;
	}

}
