package jp.co.nci.iwf.endpoint.rm.rm0510;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleMenu;

/**
 *
 */
@ApplicationScoped
public class Rm0510Repository extends BaseRepository implements CodeBook {

	public List<MwmMenuEx> search(String corporationCode, String menuRoleCode, String menuRoleType, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		params.add(menuRoleType);

		List<MwmMenuEx> list = select(MwmMenuEx.class, getSql("RM0111_01"), params.toArray());
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
