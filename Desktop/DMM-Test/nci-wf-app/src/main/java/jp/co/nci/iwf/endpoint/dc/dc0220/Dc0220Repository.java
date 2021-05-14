package jp.co.nci.iwf.endpoint.dc.dc0220;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigResult;

/**
 * 文書トレイ編集リポジトリ
 */
@ApplicationScoped
public class Dc0220Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** 文書業務管理項目名一覧を抽出 */
	public List<MwmDocBusinessInfoName> getDocBusinessInfos(String localeCode, String corporationCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmDocBusinessInfoName.class, getSql("DC0210_02"), params);
	}

	/** 文書トレイ設定を抽出 */
	public Dc0220Entity getEntity(long docTrayConfigId, String localeCode) {
		final Object[] params = { localeCode, docTrayConfigId };
		final Dc0220Entity entity = selectOne(Dc0220Entity.class, getSql("DC0210_03"), params);
		return entity;
	}

	/** 文書トレイ設定検索条件を抽出 */
	public List<Dc0220Condition> getConditions(long docTrayConfigId, String localeCode) {
		final Object[] params = { localeCode, docTrayConfigId };
		return select(Dc0220Condition.class, getSql("DC0210_04"), params);
	}

	/** 文書トレイ設定検索結果を抽出 */
	public List<Dc0220Result> getResults(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		return select(Dc0220Result.class, getSql("DC0210_05"), params);
	}

	public MwmDocTrayConfig getMwmDocTrayConfig(long docTrayConfigId) {
		return em.find(MwmDocTrayConfig.class, docTrayConfigId);
	}

	public List<MwmDocTrayConfigCondition> getMwmDocTrayConfigConditions(long docTrayConfigId) {
		final Object[] params = { docTrayConfigId };
		return select(MwmDocTrayConfigCondition.class, getSql("DC0210_06"), params);
	}

	public Collection<MwmDocTrayConfigResult> getMwmDocTrayConfigResults(long docTrayConfigId) {
		final Object[] params = { docTrayConfigId };
		return select(MwmDocTrayConfigResult.class, getSql("DC0210_07"), params);
	}

	private String defaultIfEmpty(String s, String defaultValue) {
		return isEmpty(s) ? defaultValue : s;
	}

	/** トレイ表示設定の差分更新 */
	public long saveEntity(Dc0220SaveRequest req, String localeCode, String corporationCode) {
		Dc0220Entity input = req.entity;
		MwmDocTrayConfig tc = null;
		if (input.docTrayConfigId == 0L) {
			tc = new MwmDocTrayConfig();
			tc.setDocTrayConfigId(numbering.newPK(MwmDocTrayConfig.class));
			tc.setCorporationCode(corporationCode);
		}
		else {
			tc = getMwmDocTrayConfig(input.docTrayConfigId);
		}
		tc.setCorporationCode(input.corporationCode);
		tc.setDocTrayConfigCode(input.docTrayConfigCode);
		tc.setDeleteFlag(DeleteFlag.OFF);
		tc.setPageSize(input.pageSize);
		tc.setSortOrder(input.sortOrder);
		tc.setSystemFlag(defaultIfEmpty(input.systemFlag, CommonFlag.OFF));
		tc.setDocTrayConfigName(input.docTrayConfigName);
		tc.setPersonalUseFlag(input.personalUseFlag);
		tc.setVersion(input.version);

		if (tc.getVersion() == null)
			em.persist(tc);

		return tc.getDocTrayConfigId();
	}

	/** 検索条件の差分更新 */
	public void saveConditions(Dc0220SaveRequest req, long docTrayConfigId) {
		final Map<Long, MwmDocTrayConfigCondition> currents =
				getMwmDocTrayConfigConditions(docTrayConfigId)
				.stream()
				.collect(Collectors.toMap(c -> c.getDocTrayConfigConditionId(), c -> c));
		for (Dc0220Condition input : req.conditions) {
			// 使用中のモノを消し込んでいく
			MwmDocTrayConfigCondition current = currents.remove(input.docTrayConfigConditionId);
			if (current == null) {
				current = new MwmDocTrayConfigCondition();
				current.setDocTrayConfigConditionId(numbering.newPK(MwmDocTrayConfigCondition.class));
				current.setDocTrayConfigId(docTrayConfigId);
				current.setCorporationCode(input.corporationCode);
			}
			current.setDocBusinessInfoCode(input.docBusinessInfoCode);
			current.setConditionMatchType(input.conditionMatchType);
			current.setDeleteFlag(DeleteFlag.OFF);
			current.setSortOrder(input.sortOrder);
			current.setTrayInitValue1(input.trayInitValue1);
			current.setTrayInitValue2(input.trayInitValue2);
			current.setTrayInitValue3(input.trayInitValue3);
			current.setTrayInitValue4(input.trayInitValue4);

			if (current.getVersion() == null)
				em.persist(current);
		}
		// 残余は不要なので削除
		for (MwmDocTrayConfigCondition current : currents.values()) {
			em.remove(current);
		}
	}

	/** 検索結果の差分更新 */
	public void saveResults(Dc0220SaveRequest req, long docTrayConfigId) {
		final Map<Long, MwmDocTrayConfigResult> currents =
				getMwmDocTrayConfigResults(docTrayConfigId)
				.stream()
				.collect(Collectors.toMap(c -> c.getDocTrayConfigResultId(), c -> c));
		for (Dc0220Result input : req.results) {
			// 使用中のモノを消し込んでいく
			MwmDocTrayConfigResult current = currents.remove(input.docTrayConfigResultId);
			if (current == null) {
				current = new MwmDocTrayConfigResult();
				current.setDocTrayConfigResultId(numbering.newPK(MwmDocTrayConfigResult.class));
				current.setDocTrayConfigId(docTrayConfigId);
				current.setCorporationCode(input.corporationCode);
			}
			current.setAlignType(input.alignType);
			current.setColWidth(input.colWidth);
			current.setDocBusinessInfoCode(input.docBusinessInfoCode);
			current.setDeleteFlag(DeleteFlag.OFF);
			current.setInitialSortDescFlag(defaultIfEmpty(input.initialSortDescFlag, CommonFlag.OFF));
			current.setInitialSortFlag(defaultIfEmpty(input.initialSortFlag, CommonFlag.OFF));
			current.setLinkFlag(defaultIfEmpty(input.linkFlag, CommonFlag.OFF));
			current.setSortOrder(input.sortOrder);

			if (current.getVersion() == null)
				em.persist(current);
		}
		// 残余は不要なので削除
		for (MwmDocTrayConfigResult current : currents.values()) {
			em.remove(current);
		}
	}

	public void removeMwmDocTrayConfig(Long docTrayConfigId, Long version) {
		MwmDocTrayConfig entity = getMwmDocTrayConfig(docTrayConfigId);
		if (entity != null) {
			entity.setVersion(version);
			em.remove(entity);
		}
	}

	public void removeMwmDocTrayConfigCondition(Long docTrayConfigId) {
		getMwmDocTrayConfigConditions(docTrayConfigId).forEach(entity -> em.remove(entity));
	}

	public void removeMwmDocTrayConfigResult(Long docTrayConfigId) {
		getMwmDocTrayConfigResults(docTrayConfigId).forEach(entity -> em.remove(entity));
	}

	/** 自レコードの以外で同一トレイ設定コード存在するか */
	public boolean existsDocTrayCongifCode(String corporationCode, String docTrayConfigCode, long excludeDocTrayConfigId) {
		final Object[] params = { corporationCode, docTrayConfigCode, excludeDocTrayConfigId };
		final int cnt = count(getSql("DC0210_11"), params);
		return cnt > 0;
	}
}
