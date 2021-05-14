package jp.co.nci.iwf.endpoint.na.na0000;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請フォルダ設定画面リポジトリ
 */
@ApplicationScoped
public class Na0000Repository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	public List<MwmScreenProcessLevel> getScreenProcessLevels(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("NA0000_01"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);

		return select(MwmScreenProcessLevel.class, sql.toString(), params.toArray());
	}

	public List<MwmScreenProcessDef> getScreenProcessDefs(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("NA0000_02"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		return select(MwmScreenProcessDef.class, sql.toString(), params.toArray());
	}

	public void insert(MwmScreenProcessLevel spl) {
		spl.setScreenProcessLevelId(numbering.newPK(MwmScreenProcessLevel.class));
		spl.setLevelCode(String.format("%04d", spl.getScreenProcessLevelId()));
		if (isEmpty(spl.getParentLevelCode())) {
			spl.setParentLevelCode(spl.getLevelCode());
		}
		em.persist(spl);
		em.flush();
	}

	public MwmScreenProcessLevel getLevel(Long screenProcessLevelId) {
		return em.find(MwmScreenProcessLevel.class, screenProcessLevelId);
	}

	public MwmScreenProcessDef getDef(Long screenProcessId) {
		return em.find(MwmScreenProcessDef.class, screenProcessId);
	}

	public void update(MwmScreenProcessLevel spl) {
		em.merge(spl);
		em.flush();
	}

	public void delete(MwmScreenProcessLevel spl) {
		em.remove(spl);
		em.flush();
	}

	public void update(MwmScreenProcessDef spd) {
		em.merge(spd);
		em.flush();
	}

}