package jp.co.nci.iwf.endpoint.rm.rm0111;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleMenu;

/**
 *
 */
@ApplicationScoped
public class Rm0111Repository extends BaseRepository implements CodeBook {

	private static final String MENU_ROLE_USER_ADMIN = "UserAdmin";

	public List<MwmMenuEx> search(String corporationCode, String menuRoleCode, String menuRoleType, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		final String replace;
		if (eq(MENU_ROLE_USER_ADMIN, menuRoleCode)) {
			replace = toInListSql("b.MENU_ROLE_TYPE", 2);
			params.add(menuRoleType);
			params.add(MenuRoleType.CORPORATION);
		} else {
			replace = " b.MENU_ROLE_TYPE = ? ";
			params.add(menuRoleType);
		}

		List<MwmMenuEx> list = select(MwmMenuEx.class, getSql("RM0111_01").replaceFirst("###REPLACE###", replace), params.toArray());
		return list;
	}

	public MwmAccessibleMenu insert(final MwmAccessibleMenu entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}

	public void delete(String corporationCode, String menuRoleCode) {
		List<String> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(menuRoleCode);
		execSql(getSql("RM0111_02"), params.toArray());
	}
}
