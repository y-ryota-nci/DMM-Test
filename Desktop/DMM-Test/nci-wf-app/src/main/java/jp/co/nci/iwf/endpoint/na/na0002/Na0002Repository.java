package jp.co.nci.iwf.endpoint.na.na0002;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.endpoint.na.na0002.entity.AccessibleScreenEntity;
import jp.co.nci.iwf.endpoint.na.na0002.entity.ScreenProcessLevelDefEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleScreen;

@ApplicationScoped
public class Na0002Repository extends BaseRepository implements CodeBook {

	public List<AccessibleScreenEntity> getAccessibleScreen(String corporationCode, String menuRoleCode, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		return select(AccessibleScreenEntity.class, getSql("NA0002_01"), params.toArray());
	}

	public List<ScreenProcessLevelDefEntity> getScreenProcessLevelDef(String corporationCode, String menuRoleCode, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		params.add(corporationCode);
		return select(ScreenProcessLevelDefEntity.class, getSql("NA0002_02"), params.toArray());
	}

	public void insert(final MwmAccessibleScreen entity) {
		em.persist(entity);
		em.flush();
	}

	public MwmAccessibleScreen getAccessibleScreen(long accessibleScreenId) {
		return em.find(MwmAccessibleScreen.class, accessibleScreenId);
	}

	public void update(final MwmAccessibleScreen entity) {
		em.merge(entity);
		em.flush();
	}

	public void delete(MwmAccessibleScreen entity) {
		em.remove(entity);
		em.flush();
	}

	public MwvScreenProcessDef getMwvScreenProcess(Long screenProcessId, String localeCode) {
		final Object[] params = { screenProcessId, localeCode };
		return selectOne(MwvScreenProcessDef.class, getSql("NA0002_03"), params);
	}

	/** 自分自身を除く同一キーのレコードがすでに存在するか */
	public boolean isExists(MwmAccessibleScreen as) {
		final StringBuilder sql = new StringBuilder(getSql("NA0002_04"));
		final List<Object> params = new ArrayList<>();
		params.add(as.getCorporationCode());
		params.add(as.getMenuRoleCode());
		params.add(as.getScreenProcessId());
		if (as.getAccessibleScreenId() != 0L) {
			sql.append(" and ACCESSIBLE_SCREEN_ID != ? ");
			params.add(as.getAccessibleScreenId());
		}
		int count = count(sql.toString(), params.toArray());
		return count > 0;
	}

	/**
	 * 画面プロセス配下のコンテナを抽出
	 * @param screenProcessId 画面プロセスID
	 * @param localeCode
	 * @return
	 */
	public List<MwvContainer> getMwvContainers(long screenProcessId, String localeCode) {
		final Object[] params = { localeCode, screenProcessId };
		return select(MwvContainer.class, getSql("NA0002_05"), params);
	}
}
