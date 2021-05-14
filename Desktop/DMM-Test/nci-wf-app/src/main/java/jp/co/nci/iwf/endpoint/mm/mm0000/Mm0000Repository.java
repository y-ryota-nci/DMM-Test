package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * プロファイル管理画面リポジトリ
 */
@ApplicationScoped
public class Mm0000Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/**
	 * メニューロールCDで選択可能なメニューをすべて抽出する
	 * @param corporationCode
	 * @param menuRoleCode
	 * @return
	 */
	public List<MwmMenu> getMwmMenus(String corporationCode, String menuRoleCode, String menuRoleType) {
		final Object[] params = { menuRoleType, corporationCode, menuRoleCode };
		return select(MwmMenu.class, getSql("MM0000_01"), params);
	}

	/**
	 * メニューロールCD＋対象メニューでアクセス可能メニューマスタをインサート
	 * @param corporationCode
	 * @param menuRoleCode
	 * @param menus
	 */
	public void insertMwmAccessibleMenu(String corporationCode, String menuRoleCode, List<MwmMenu> menus, java.sql.Date validStartDate) {
		for (MwmMenu menu : menus) {
			final MwmAccessibleMenu m = new MwmAccessibleMenu();
			m.setAccessibleMenuId(numbering.newPK(MwmAccessibleMenu.class));
			m.setCorporationCode(corporationCode);
			m.setDeleteFlag(DeleteFlag.OFF);
			m.setMenuId(menu.getMenuId());
			m.setMenuRoleCode(menuRoleCode);
			m.setValidStartDate(validStartDate);
			m.setValidEndDate(ENDDATE);
			em.persist(m);
		}
	}

}
