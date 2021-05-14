package jp.co.nci.iwf.endpoint.dc.dc0131;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0131.entity.AccessibleDocEntity;
import jp.co.nci.iwf.endpoint.dc.dc0131.entity.ScreenDocLevelDefEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleDoc;

/**
 * 業務文書公開設定画面のリポジトリ
 */
@ApplicationScoped
public class Dc0131Repository extends BaseRepository implements CodeBook {

	public List<AccessibleDocEntity> getAccessibleDoc(String corporationCode, String menuRoleCode, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		return select(AccessibleDocEntity.class, getSql("DC0131_01"), params.toArray());
	}

	public List<ScreenDocLevelDefEntity> getScreenDocLevelDef(String corporationCode, String menuRoleCode, String localeCode) {
		List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(localeCode);
		params.add(corporationCode);
		params.add(menuRoleCode);
		params.add(corporationCode);
		return select(ScreenDocLevelDefEntity.class, getSql("DC0131_02"), params.toArray());
	}

	public void insert(final MwmAccessibleDoc entity) {
		em.persist(entity);
		em.flush();
	}

	public MwmAccessibleDoc getAccessibleDoc(long accessibleDocId) {
		return em.find(MwmAccessibleDoc.class, accessibleDocId);
	}

	public void update(final MwmAccessibleDoc entity) {
		em.merge(entity);
		em.flush();
	}

	public void delete(MwmAccessibleDoc entity) {
		em.remove(entity);
		em.flush();
	}

	public MwvScreenDocDef getMwvScreenDoc(Long screenDocId, String localeCode) {
		final Object[] params = { screenDocId, localeCode };
		return selectOne(MwvScreenDocDef.class, getSql("DC0131_03"), params);
	}

	/** 自分自身を除く同一キーのレコードがすでに存在するか */
	public boolean isExists(MwmAccessibleDoc ad) {
		final StringBuilder sql = new StringBuilder(getSql("DC0131_04"));
		final List<Object> params = new ArrayList<>();
		params.add(ad.getCorporationCode());
		params.add(ad.getMenuRoleCode());
		params.add(ad.getScreenDocId());
		if (ad.getAccessibleDocId() != 0L) {
			sql.append(" and ACCESSIBLE_DOC_ID != ? ");
			params.add(ad.getAccessibleDocId());
		}
		int count = count(sql.toString(), params.toArray());
		return count > 0;
	}

	/**
	 * 画面プロセス配下のコンテナを抽出
	 * @param screenDocId 画面文書ID
	 * @param localeCode
	 * @return
	 */
	public List<MwvContainer> getMwvContainers(long screenDocId, String localeCode) {
		final Object[] params = { localeCode, screenDocId };
		return select(MwvContainer.class, getSql("DC0131_05"), params);
	}
}
