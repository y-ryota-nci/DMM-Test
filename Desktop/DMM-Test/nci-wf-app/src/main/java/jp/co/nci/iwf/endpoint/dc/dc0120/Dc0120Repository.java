package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 業務文書メニュー編集画面リポジトリ
 */
@ApplicationScoped
public class Dc0120Repository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	public List<MwmScreenDocLevel> getScreenDocLevels(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0120_01"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);

		return select(MwmScreenDocLevel.class, sql.toString(), params.toArray());
	}

	public List<MwmScreenDocDef> getScreenDocDefs(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0120_02"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		return select(MwmScreenDocDef.class, sql.toString(), params.toArray());
	}

	public void insert(MwmScreenDocLevel spl) {
		spl.setScreenDocLevelId(numbering.newPK(MwmScreenDocLevel.class));
		spl.setLevelCode(String.format("%04d", spl.getScreenDocLevelId()));
		if (isEmpty(spl.getParentLevelCode())) {
			spl.setParentLevelCode(spl.getLevelCode());
		}
		em.persist(spl);
		em.flush();
	}

	public MwmScreenDocLevel getLevel(Long screenDocLevelId) {
		return em.find(MwmScreenDocLevel.class, screenDocLevelId);
	}

	public MwmScreenDocDef getDef(Long screenDocId) {
		return em.find(MwmScreenDocDef.class, screenDocId);
	}

	public void update(MwmScreenDocLevel spl) {
		em.merge(spl);
		em.flush();
	}

	public void delete(MwmScreenDocLevel spl) {
		em.remove(spl);
		em.flush();
	}

	public void update(MwmScreenDocDef spd) {
		em.merge(spd);
		em.flush();
	}

}